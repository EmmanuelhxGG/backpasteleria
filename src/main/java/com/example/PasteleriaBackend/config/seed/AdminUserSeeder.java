package com.example.PasteleriaBackend.config.seed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.Role;
import com.example.PasteleriaBackend.domain.model.RoleName;
import com.example.PasteleriaBackend.domain.model.User;
import com.example.PasteleriaBackend.domain.repository.RoleRepository;
import com.example.PasteleriaBackend.domain.repository.UserRepository;

@Component
@Order(20)
@Transactional
public class AdminUserSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${APP_ADMIN_EMAIL:admin@pasteleria.local}")
    private String adminEmail;

    @Value("${APP_ADMIN_PASSWORD:Admin1234!}")
    private String adminPassword;

    @Value("${APP_ADMIN_RUN:99999999-9}")
    private String adminRun;

    public AdminUserSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.existsByEmailIgnoreCase(adminEmail)) {
            return;
        }

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
            .orElseThrow(() -> new IllegalStateException("No se encontró el rol ADMIN para inicializar el usuario maestro"));

        User admin = new User();
        admin.setRun(adminRun);
        admin.setFirstName("Administrador");
        admin.setLastName("General");
        admin.setEmail(adminEmail.toLowerCase());
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setRole(adminRole);
        admin.setCustomerType(null);
        admin.setDefaultShippingCost(0d);
        admin.setNewsletter(false);
        admin.setSaveAddress(false);
        admin.setStaffRole("Administrador");
        admin.setFelices50(false);

        userRepository.save(admin);
    }
}
