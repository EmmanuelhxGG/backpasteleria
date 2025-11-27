package com.example.PasteleriaBackend.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PasteleriaBackend.domain.model.Role;
import com.example.PasteleriaBackend.domain.model.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
