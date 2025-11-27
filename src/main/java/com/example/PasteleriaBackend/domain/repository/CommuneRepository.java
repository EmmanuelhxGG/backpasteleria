package com.example.PasteleriaBackend.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PasteleriaBackend.domain.model.Commune;

public interface CommuneRepository extends JpaRepository<Commune, Long> {

    List<Commune> findByRegionId(Long regionId);
}
