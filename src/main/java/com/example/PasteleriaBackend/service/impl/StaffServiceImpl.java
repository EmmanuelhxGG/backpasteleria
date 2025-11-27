package com.example.PasteleriaBackend.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.PasteleriaBackend.domain.model.Role;
import com.example.PasteleriaBackend.domain.model.RoleName;
import com.example.PasteleriaBackend.domain.model.User;
import com.example.PasteleriaBackend.domain.model.UserStatus;
import com.example.PasteleriaBackend.domain.repository.RoleRepository;
import com.example.PasteleriaBackend.domain.repository.UserRepository;
import com.example.PasteleriaBackend.service.StaffService;
import com.example.PasteleriaBackend.web.dto.StaffRequest;
import com.example.PasteleriaBackend.web.dto.StaffResponse;
import com.example.PasteleriaBackend.web.dto.StaffUpdateRequest;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StaffServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public StaffResponse createStaff(StaffRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalStateException("El correo ya está registrado");
        }

        Role role = roleRepository.findByName(RoleName.ADMIN)
            .orElseThrow(() -> new IllegalStateException("No existe el rol ADMIN"));

        User user = new User();
        user.setRole(role);
        user.setRun(request.run().trim());
        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password())) ;
        user.setStaffRole(trimToNullValue(request.staffRole()));
        user.setRegion(trimToNullValue(request.region()));
        user.setCommune(trimToNullValue(request.commune()));
        user.setAddress(trimToNullValue(request.address()));
        user.setPhone(trimToNullValue(request.phone()));
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
        return toStaffResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResponse> listStaff() {
        return userRepository.findByRoleNameOrderByFirstNameAsc(RoleName.ADMIN).stream()
            .map(this::toStaffResponse)
            .collect(Collectors.toList());
    }

    @Override
    public StaffResponse updateStaff(String staffId, StaffUpdateRequest request) {
        User user = findStaffById(staffId);

        trimToNull(request.firstName()).ifPresent(user::setFirstName);
        trimToNull(request.lastName()).ifPresent(user::setLastName);
        trimToNull(request.staffRole()).ifPresent(user::setStaffRole);
        trimToNull(request.region()).ifPresent(user::setRegion);
        trimToNull(request.commune()).ifPresent(user::setCommune);
        trimToNull(request.address()).ifPresent(user::setAddress);
        trimToNull(request.phone()).ifPresent(user::setPhone);

        if (StringUtils.hasText(request.email())) {
            String nextEmail = normalizeEmail(request.email());
            if (!nextEmail.equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmailIgnoreCase(nextEmail)) {
                throw new IllegalStateException("El correo ya está registrado");
            }
            user.setEmail(nextEmail);
        }

        if (StringUtils.hasText(request.password())) {
            user.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        if (request.active() != null) {
            user.setStatus(request.active() ? UserStatus.ACTIVE : UserStatus.INACTIVE);
        }

        return toStaffResponse(user);
    }

    @Override
    public void deleteStaff(String staffId) {
        User user = findStaffById(staffId);
        if (user.getStatus() == UserStatus.ACTIVE
            && userRepository.countByRole_NameAndStatus(RoleName.ADMIN, UserStatus.ACTIVE) <= 1) {
            throw new IllegalStateException("No se puede eliminar el único administrador activo");
        }
        userRepository.delete(user);
    }

    private User findStaffById(String staffId) {
        UUID id;
        try {
            id = UUID.fromString(staffId);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Identificador de usuario inválido", ex);
        }
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if (user.getRole() == null || user.getRole().getName() != RoleName.ADMIN) {
            throw new IllegalStateException("La operación solo aplica para personal administrativo");
        }
        return user;
    }

    private StaffResponse toStaffResponse(User user) {
        return new StaffResponse(
            user.getId().toString(),
            user.getRun(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getStaffRole(),
            user.getRegion(),
            user.getCommune(),
            user.getAddress(),
            user.getPhone(),
            user.getStatus()
        );
    }

    private String normalizeEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("El correo es obligatorio");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private java.util.Optional<String> trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(value.trim());
    }

    private String trimToNullValue(String value) {
        return trimToNull(value).orElse(null);
    }
}
