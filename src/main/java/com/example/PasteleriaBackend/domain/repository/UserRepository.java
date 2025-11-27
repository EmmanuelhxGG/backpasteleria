package com.example.PasteleriaBackend.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.PasteleriaBackend.domain.model.RoleName;
import com.example.PasteleriaBackend.domain.model.User;
import com.example.PasteleriaBackend.domain.model.UserStatus;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findWithRoleByEmailIgnoreCase(String email);

    long countByStatus(UserStatus status);

    @EntityGraph(attributePaths = {"role", "addresses"})
    List<User> findByRoleNameOrderByFirstNameAsc(RoleName roleName);

    long countByRole_NameAndStatus(RoleName roleName, UserStatus status);
}
