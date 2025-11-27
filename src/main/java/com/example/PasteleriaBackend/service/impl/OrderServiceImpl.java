package com.example.PasteleriaBackend.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.PasteleriaBackend.domain.model.Coupon;
import com.example.PasteleriaBackend.domain.model.Order;
import com.example.PasteleriaBackend.domain.model.OrderItem;
import com.example.PasteleriaBackend.domain.model.RoleName;
import com.example.PasteleriaBackend.domain.model.User;
import com.example.PasteleriaBackend.domain.model.UserAddress;
import com.example.PasteleriaBackend.domain.model.UserStatus;
import com.example.PasteleriaBackend.domain.repository.CouponRepository;
import com.example.PasteleriaBackend.domain.repository.OrderRepository;
import com.example.PasteleriaBackend.domain.repository.ProductRepository;
import com.example.PasteleriaBackend.domain.repository.UserAddressRepository;
import com.example.PasteleriaBackend.domain.repository.UserRepository;
import com.example.PasteleriaBackend.service.OrderService;
import com.example.PasteleriaBackend.web.dto.CreateOrderItemRequest;
import com.example.PasteleriaBackend.web.dto.CreateOrderRequest;
import com.example.PasteleriaBackend.web.dto.OrderItemResponse;
import com.example.PasteleriaBackend.web.dto.OrderResponse;
import com.example.PasteleriaBackend.web.dto.UpdateOrderStatusRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<List<String>>() { };

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;
    private final ObjectMapper objectMapper;

    public OrderServiceImpl(
        OrderRepository orderRepository,
        ProductRepository productRepository,
        CouponRepository couponRepository,
        UserRepository userRepository,
        UserAddressRepository userAddressRepository,
        ObjectMapper objectMapper
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.userAddressRepository = userAddressRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public OrderResponse createOrder(String userId, CreateOrderRequest request) {
        User customer = findUserById(userId);
        ensureCustomer(customer);

        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("La orden debe incluir al menos un producto");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderCode(generateOrderCode());

        double discountedSubtotal = 0d;
        double discountTotal = 0d;

        for (CreateOrderItemRequest itemRequest : request.items()) {
            OrderItem item = buildOrderItem(itemRequest);
            order.addItem(item);

            discountedSubtotal += safeMultiply(item.getUnitPrice(), item.getQuantity());
            discountTotal += safeMultiply(item.getDiscountPerUnit(), item.getQuantity());

            // Ajustamos stock básico para reflejar la venta
            item.getProduct().setStock(Math.max(0, item.getProduct().getStock() - item.getQuantity()));
        }

        double shippingCost = normalizeAmount(request.shippingCost(), customer.getDefaultShippingCost());
        order.setSubtotal(round(discountedSubtotal));
        order.setDiscountTotal(round(discountTotal));
        order.setShippingCost(round(shippingCost));
        order.setTotal(round(discountedSubtotal + shippingCost));
        order.setBenefitsApplied(writeJsonArray(request.benefitsApplied()));
        order.setNotes(trimToNull(request.notes()));

        if (StringUtils.hasText(request.couponCode())) {
            Coupon coupon = couponRepository.findByCodeIgnoreCase(request.couponCode().trim())
                .filter(Coupon::isActive)
                .orElseThrow(() -> new IllegalArgumentException("Cupón inválido o inactivo"));
            order.setCoupon(coupon);
        }

        if (request.shippingAddressId() != null) {
            UserAddress address = userAddressRepository.findById(request.shippingAddressId())
                .orElseThrow(() -> new IllegalArgumentException("Dirección de envío no encontrada"));
            if (!address.getUser().getId().equals(customer.getId())) {
                throw new IllegalArgumentException("La dirección seleccionada no pertenece al cliente");
            }
            order.setShippingAddress(address);
        }

        orderRepository.save(order);
        orderRepository.flush();

        Order persisted = orderRepository.findDetailedById(order.getId()).orElse(order);
        return toResponse(persisted);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findOrdersForCustomer(String userId) {
        UUID customerId = parseUuid(userId, "Identificador de usuario inválido");
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateStatus(String orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findDetailedById(parseUuid(orderId, "Identificador de orden inválido"))
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));
        order.setStatus(request.status());
        if (request.notes() != null) {
            order.setNotes(trimToNull(request.notes()));
        }
        return toResponse(order);
    }

    private OrderItem buildOrderItem(CreateOrderItemRequest itemRequest) {
        String productId = StringUtils.hasText(itemRequest.productId()) ? itemRequest.productId().trim() : null;
        if (!StringUtils.hasText(productId)) {
            throw new IllegalArgumentException("Cada producto debe incluir un identificador");
        }

        var product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + productId));

        if (!product.isActive()) {
            throw new IllegalStateException("El producto " + product.getName() + " no se encuentra activo");
        }

        int quantity = itemRequest.quantity();
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        if (product.getStock() < quantity) {
            throw new IllegalStateException("Stock insuficiente para " + product.getName());
        }

        Double unitPrice = Objects.requireNonNull(itemRequest.unitPrice(), "El precio unitario es obligatorio");
        Double originalUnitPrice = Objects.requireNonNull(itemRequest.originalUnitPrice(), "El precio original es obligatorio");
        Double discountPerUnit = Objects.requireNonNull(itemRequest.discountPerUnit(), "El descuento por unidad es obligatorio");

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setProductName(product.getName());
        item.setQuantity(quantity);
        item.setUnitPrice(round(unitPrice));
        item.setOriginalUnitPrice(round(originalUnitPrice));
        item.setDiscountPerUnit(round(discountPerUnit));
        item.setSubtotal(round(unitPrice * quantity));
        item.setOriginalSubtotal(round(originalUnitPrice * quantity));
        item.setBenefitLabels(writeJsonArray(itemRequest.benefitLabels()));
        item.setNote(trimToNull(itemRequest.note()));

        return item;
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
            .sorted(Comparator.comparing(OrderItem::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
            .map(this::toItemResponse)
            .collect(Collectors.toList());

        List<String> appliedBenefits = readJsonArray(order.getBenefitsApplied());
        String couponCode = order.getCoupon() != null ? order.getCoupon().getCode() : null;
        String couponLabel = order.getCoupon() != null ? order.getCoupon().getLabel() : null;
        User customer = order.getCustomer();
        String customerName = String.join(" ",
            List.of(
                StringUtils.hasText(customer.getFirstName()) ? customer.getFirstName() : "",
                StringUtils.hasText(customer.getLastName()) ? customer.getLastName() : ""
            )).trim();

        return new OrderResponse(
            order.getId().toString(),
            order.getOrderCode(),
            order.getStatus(),
            customerName.isBlank() ? customer.getEmail() : customerName,
            customer.getEmail(),
            round(order.getSubtotal()),
            round(order.getDiscountTotal()),
            round(order.getShippingCost()),
            round(order.getTotal()),
            appliedBenefits,
            couponCode,
            couponLabel,
            order.getCreatedAt() != null ? order.getCreatedAt().toInstant().toEpochMilli() : 0L,
            items
        );
    }

    private OrderItemResponse toItemResponse(OrderItem item) {
        List<String> labels = readJsonArray(item.getBenefitLabels());
        return new OrderItemResponse(
            item.getProduct().getId(),
            item.getProductName(),
            item.getQuantity(),
            round(item.getUnitPrice()),
            round(item.getOriginalUnitPrice()),
            round(item.getDiscountPerUnit()),
            round(item.getSubtotal()),
            round(item.getOriginalSubtotal()),
            labels
        );
    }

    private User findUserById(String userId) {
        UUID id = parseUuid(userId, "Identificador de usuario inválido");
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    private void ensureCustomer(User user) {
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("El usuario no está habilitado para comprar");
        }
        if (user.getRole() == null || user.getRole().getName() != RoleName.CUSTOMER) {
            throw new IllegalStateException("Solo los usuarios con perfil de cliente pueden generar órdenes");
        }
    }

    private UUID parseUuid(String value, String message) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException(message, ex);
        }
    }

    private String generateOrderCode() {
        int attempts = 0;
        while (attempts < 5) {
            String candidate = "PED-" + String.format(Locale.ROOT, "%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
            if (orderRepository.findByOrderCode(candidate).isEmpty()) {
                return candidate;
            }
            attempts++;
        }
        return "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
    }

    private double normalizeAmount(Double value, Double fallback) {
        double base = value != null ? value : (fallback != null ? fallback : 0d);
        if (Double.isNaN(base) || base < 0) {
            throw new IllegalArgumentException("El costo de envío no puede ser negativo");
        }
        return base;
    }

    private double safeMultiply(Double value, Integer quantity) {
        return (value != null ? value : 0d) * (quantity != null ? quantity : 0);
    }

    private double round(Double value) {
        if (value == null) {
            return 0d;
        }
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private String writeJsonArray(List<String> values) {
        List<String> sanitized = values == null ? Collections.emptyList()
            : values.stream().filter(StringUtils::hasText).map(String::trim).collect(Collectors.toList());
        try {
            return objectMapper.writeValueAsString(sanitized);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No se pudo serializar la lista de beneficios", ex);
        }
    }

    private List<String> readJsonArray(String json) {
        if (!StringUtils.hasText(json)) {
            return List.of();
        }
        try {
            return objectMapper.readValue(json, STRING_LIST_TYPE);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No se pudo leer la lista de beneficios", ex);
        }
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }
}
