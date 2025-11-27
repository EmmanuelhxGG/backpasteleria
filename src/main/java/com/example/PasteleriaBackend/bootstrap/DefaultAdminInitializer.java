package com.example.PasteleriaBackend.bootstrap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.Role;
import com.example.PasteleriaBackend.domain.model.RoleName;
import com.example.PasteleriaBackend.domain.model.User;
import com.example.PasteleriaBackend.domain.model.UserStatus;
import com.example.PasteleriaBackend.domain.repository.RoleRepository;
import com.example.PasteleriaBackend.domain.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DefaultAdminInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.bootstrap.admin-email:admin@duoc.cl}")
    private String adminEmail;

    @Value("${app.bootstrap.admin-password:1234}")
    private String adminPassword;

    public DefaultAdminInitializer(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        var adminRole = roleRepository.findByName(RoleName.ADMIN)
            .orElseGet(() -> roleRepository.save(buildAdminRole()));

        userRepository.findWithRoleByEmailIgnoreCase(adminEmail)
            .ifPresentOrElse(
                user -> refreshExistingAdmin(user, adminRole),
                () -> createDefaultAdmin(adminRole)
            );
    }

    private Role buildAdminRole() {
        Role role = new Role();
        role.setName(RoleName.ADMIN);
        return role;
    }

    private void refreshExistingAdmin(User user, Role adminRole) {
        user.setRole(adminRole);
        user.setStatus(UserStatus.ACTIVE);
        user.setPasswordHash(passwordEncoder.encode(adminPassword));
        if (user.getFirstName() == null || user.getFirstName().isBlank()) {
            user.setFirstName("Administrador");
        }
        if (user.getLastName() == null || user.getLastName().isBlank()) {
            user.setLastName("Principal");
        }
        if (user.getRun() == null || user.getRun().isBlank()) {
            user.setRun("11111111-1");
        }
    }

    private void createDefaultAdmin(Role adminRole) {
        User user = new User();
        user.setEmail(adminEmail);
        user.setPasswordHash(passwordEncoder.encode(adminPassword));
        user.setRole(adminRole);
        user.setStatus(UserStatus.ACTIVE);
        user.setFirstName("Administrador");
        user.setLastName("Principal");
        user.setRun("11111111-1");
        user.setCustomerType("Administrador");
        user.setNewsletter(false);
        user.setSaveAddress(false);
        user.setFelices50(false);
        userRepository.save(user);
    }
}
