package com.example.PasteleriaBackend.config.seed;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.Role;
import com.example.PasteleriaBackend.domain.model.RoleName;
import com.example.PasteleriaBackend.domain.repository.RoleRepository;

@Component
@Order(10)
@Transactional
public class RoleSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleName);
                return roleRepository.save(role);
            });
        }
    }
}
