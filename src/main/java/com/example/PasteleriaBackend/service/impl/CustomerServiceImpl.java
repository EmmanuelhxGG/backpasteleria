package com.example.PasteleriaBackend.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.PasteleriaBackend.domain.model.Role;
import com.example.PasteleriaBackend.domain.model.RoleName;
import com.example.PasteleriaBackend.domain.model.User;
import com.example.PasteleriaBackend.domain.model.UserAddress;
import com.example.PasteleriaBackend.domain.model.UserStatus;
import com.example.PasteleriaBackend.domain.repository.RoleRepository;
import com.example.PasteleriaBackend.domain.repository.UserAddressRepository;
import com.example.PasteleriaBackend.domain.repository.UserRepository;
import com.example.PasteleriaBackend.service.CustomerService;
import com.example.PasteleriaBackend.web.dto.CustomerAddressRequest;
import com.example.PasteleriaBackend.web.dto.CustomerAddressResponse;
import com.example.PasteleriaBackend.web.dto.CustomerProfileResponse;
import com.example.PasteleriaBackend.web.dto.CustomerRegistrationRequest;
import com.example.PasteleriaBackend.web.dto.CustomerSummaryResponse;
import com.example.PasteleriaBackend.web.dto.CustomerUpdateRequest;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final double DEFAULT_SHIPPING_COST = 3000d;
    private static final String FELICES50 = "FELICES50";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserAddressRepository userAddressRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(
        UserRepository userRepository,
        RoleRepository roleRepository,
        UserAddressRepository userAddressRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userAddressRepository = userAddressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomerProfileResponse register(CustomerRegistrationRequest request) {
        String email = trimToNull(request.email())
            .orElseThrow(() -> new IllegalArgumentException("El correo es obligatorio"));
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalStateException("El correo ya está registrado");
        }

        Role role = roleRepository.findByName(RoleName.CUSTOMER)
            .orElseThrow(() -> new IllegalStateException("No existe el rol CUSTOMER"));

        User user = new User();
        user.setRole(role);
        user.setRun(trimToNull(request.run()).orElseThrow(() -> new IllegalArgumentException("El RUN es obligatorio")));
        user.setFirstName(trimToNull(request.firstName()).orElse(""));
        user.setLastName(trimToNull(request.lastName()).orElse(""));
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCustomerType(trimToNull(request.customerType()).orElse("Cliente"));
        user.setBirthDate(request.birthDate());
        user.setRegion(trimToNull(request.region()).orElse(null));
        user.setCommune(trimToNull(request.commune()).orElse(null));
        user.setAddress(trimToNull(request.address()).orElse(null));
        user.setPhone(trimToNull(request.phone()).orElse(null));

        String promoCode = trimToNull(request.promoCode())
            .map(value -> value.toUpperCase(Locale.ROOT))
            .orElse(null);
        user.setPromoCode(promoCode);
        user.setFelices50(FELICES50.equalsIgnoreCase(promoCode));
        user.setBirthdayRedeemedYear(request.birthdayRedeemedYear());

        user.setDefaultShippingCost(normalizeShipping(request.defaultShippingCost()));
        user.setNewsletter(Boolean.TRUE.equals(request.newsletter()));
        user.setSaveAddress(Boolean.TRUE.equals(request.saveAddress()));

        userRepository.save(user);

        applyAddresses(
            user,
            request.addresses(),
            null,
            user.isSaveAddress(),
            user.getAddress(),
            user.getRegion(),
            user.getCommune()
        );

        userRepository.flush();
        return toProfileResponse(userRepository.findById(user.getId())
            .orElseThrow(() -> new IllegalStateException("No se pudo recuperar el usuario creado")));
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerProfileResponse getCurrentProfile(String userId) {
        User user = findUserById(userId);
        return toProfileResponse(user);
    }

    @Override
    public CustomerProfileResponse updateCurrentProfile(String userId, CustomerUpdateRequest request) {
        User user = findUserById(userId);
        applyUpdatesToUser(user, request, true);
        return toProfileResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerSummaryResponse> listCustomers() {
        return userRepository.findByRoleNameOrderByFirstNameAsc(RoleName.CUSTOMER).stream()
            .map(this::toSummaryResponse)
            .collect(Collectors.toList());
    }

    @Override
    public CustomerSummaryResponse updateCustomer(String customerId, CustomerUpdateRequest request) {
        User user = findUserById(customerId);
        ensureCustomerRole(user);
        applyUpdatesToUser(user, request, false);
        return toSummaryResponse(user);
    }

    @Override
    public void updateCustomerStatus(String customerId, UserStatus status) {
        User user = findUserById(customerId);
        ensureCustomerRole(user);
        user.setStatus(status);
    }

    private User findUserById(String userId) {
        UUID id;
        try {
            id = UUID.fromString(userId);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Identificador de usuario inválido", ex);
        }
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    private void ensureCustomerRole(User user) {
        if (user.getRole() == null || user.getRole().getName() != RoleName.CUSTOMER) {
            throw new IllegalStateException("La operación solo aplica para clientes");
        }
    }

    private void applyUpdatesToUser(User user, CustomerUpdateRequest request, boolean allowEmailChange) {
        trimToNull(request.firstName()).ifPresent(user::setFirstName);
        trimToNull(request.lastName()).ifPresent(user::setLastName);
        trimToNull(request.customerType()).ifPresent(user::setCustomerType);

        if (allowEmailChange && StringUtils.hasText(request.email())) {
            String email = request.email().trim().toLowerCase(Locale.ROOT);
            if (!email.equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmailIgnoreCase(email)) {
                throw new IllegalStateException("El correo ya está registrado");
            }
            user.setEmail(email);
        }

        if (request.birthDate() != null) {
            user.setBirthDate(request.birthDate());
        }

        trimToNull(request.region()).ifPresent(user::setRegion);
        trimToNull(request.commune()).ifPresent(user::setCommune);
        trimToNull(request.address()).ifPresent(user::setAddress);
        trimToNull(request.phone()).ifPresent(user::setPhone);

        trimToNull(request.promoCode())
            .map(value -> value.toUpperCase(Locale.ROOT))
            .ifPresent(code -> {
                user.setPromoCode(code);
                if (FELICES50.equalsIgnoreCase(code)) {
                    user.setFelices50(true);
                }
            });

        if (request.felices50() != null) {
            user.setFelices50(request.felices50());
        }

        if (request.birthdayRedeemedYear() != null) {
            user.setBirthdayRedeemedYear(request.birthdayRedeemedYear());
        }

        if (request.defaultShippingCost() != null) {
            user.setDefaultShippingCost(normalizeShipping(request.defaultShippingCost()));
        }

        if (request.newsletter() != null) {
            user.setNewsletter(request.newsletter());
        }

        if (request.saveAddress() != null) {
            user.setSaveAddress(request.saveAddress());
        }

        applyAddresses(
            user,
            request.addresses(),
            request.primaryAddressId(),
            user.isSaveAddress(),
            user.getAddress(),
            user.getRegion(),
            user.getCommune()
        );
    }

    private void applyAddresses(
        User user,
        List<CustomerAddressRequest> addressRequests,
        UUID explicitPrimary,
        boolean retainAddresses,
        String fallbackAddress,
        String fallbackRegion,
        String fallbackCommune
    ) {
        if (!retainAddresses && (addressRequests == null || addressRequests.isEmpty())) {
            user.getAddresses().clear();
            return;
        }

        Map<UUID, UserAddress> existing = user.getAddresses().stream()
            .collect(Collectors.toMap(UserAddress::getId, Function.identity()));

        List<UserAddress> next = new ArrayList<>();

        if (addressRequests != null) {
            for (CustomerAddressRequest entry : addressRequests) {
                if (entry == null) {
                    continue;
                }
                String direccion = trimToNull(entry.direccion()).orElse(null);
                String region = trimToNull(entry.region()).orElse(null);
                String comuna = trimToNull(entry.comuna()).orElse(null);
                if (!StringUtils.hasText(direccion) || !StringUtils.hasText(region) || !StringUtils.hasText(comuna)) {
                    continue;
                }

                UUID id = entry.id() != null ? entry.id() : UUID.randomUUID();
                UserAddress address = existing.getOrDefault(id, new UserAddress());
                address.setUser(user);
                address.setAlias(trimToNull(entry.alias()).orElse(null));
                address.setAddress(direccion);
                address.setRegion(region);
                address.setCommune(comuna);
                address.setReferenceText(trimToNull(entry.referencia()).orElse(null));
                address.setPrimary(entry.primary());
                next.add(address);
            }
        }

        boolean hasPrimary = next.stream().anyMatch(UserAddress::isPrimary);
        if (explicitPrimary != null) {
            for (UserAddress address : next) {
                address.setPrimary(address.getId().equals(explicitPrimary));
            }
            hasPrimary = next.stream().anyMatch(UserAddress::isPrimary);
        }

        if (next.isEmpty() && retainAddresses) {
            if (StringUtils.hasText(fallbackAddress) && StringUtils.hasText(fallbackRegion) && StringUtils.hasText(fallbackCommune)) {
                UserAddress address = new UserAddress();
                address.setUser(user);
                address.setAlias("Dirección principal");
                address.setAddress(fallbackAddress);
                address.setRegion(fallbackRegion);
                address.setCommune(fallbackCommune);
                address.setPrimary(true);
                next.add(address);
                hasPrimary = true;
            }
        }

        if (!next.isEmpty() && !hasPrimary) {
            next.get(0).setPrimary(true);
        }

        user.getAddresses().clear();
        next.stream()
            .sorted(Comparator.comparing(UserAddress::isPrimary).reversed())
            .forEach(address -> user.addAddress(address));

        userAddressRepository.flush();
    }

    private CustomerProfileResponse toProfileResponse(User user) {
        List<CustomerAddressResponse> addresses = user.getAddresses().stream()
            .sorted(Comparator.comparing(UserAddress::isPrimary).reversed()
                .thenComparing(address -> Optional.ofNullable(address.getCreatedAt()).orElse(null), Comparator.nullsLast(Comparator.naturalOrder())))
            .map(this::toAddressResponse)
            .collect(Collectors.toList());

        return new CustomerProfileResponse(
            user.getId().toString(),
            user.getRun(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getStatus(),
            user.getCustomerType(),
            user.getBirthDate(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getRegion(),
            user.getCommune(),
            user.getAddress(),
            user.getPhone(),
            user.getPromoCode(),
            user.isFelices50(),
            user.getBirthdayRedeemedYear(),
            Optional.ofNullable(user.getDefaultShippingCost()).orElse(DEFAULT_SHIPPING_COST),
            user.isNewsletter(),
            user.isSaveAddress(),
            user.getStaffRole(),
            addresses
        );
    }

    private CustomerSummaryResponse toSummaryResponse(User user) {
        return new CustomerSummaryResponse(
            user.getId().toString(),
            user.getRun(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getBirthDate(),
            user.getRegion(),
            user.getCommune(),
            user.getCustomerType(),
            user.getStatus(),
            user.isNewsletter(),
            user.isFelices50(),
            user.getBirthdayRedeemedYear(),
            Optional.ofNullable(user.getDefaultShippingCost()).orElse(DEFAULT_SHIPPING_COST)
        );
    }

    private CustomerAddressResponse toAddressResponse(UserAddress address) {
        long createdAt = Optional.ofNullable(address.getCreatedAt())
            .map(time -> time.toInstant().toEpochMilli())
            .orElse(0L);
        long updatedAt = Optional.ofNullable(address.getUpdatedAt())
            .map(time -> time.toInstant().toEpochMilli())
            .orElse(createdAt);
        return new CustomerAddressResponse(
            address.getId().toString(),
            address.getAlias(),
            address.getAddress(),
            address.getRegion(),
            address.getCommune(),
            address.getReferenceText(),
            address.isPrimary(),
            createdAt,
            updatedAt
        );
    }

    private double normalizeShipping(Double value) {
        if (value == null || value.isNaN() || value < 0) {
            return DEFAULT_SHIPPING_COST;
        }
        return value;
    }

    private Optional<String> trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return Optional.empty();
        }
        return Optional.of(value.trim());
    }
}
