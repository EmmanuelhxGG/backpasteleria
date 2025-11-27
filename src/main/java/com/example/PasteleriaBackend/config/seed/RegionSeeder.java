package com.example.PasteleriaBackend.config.seed;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.model.Commune;
import com.example.PasteleriaBackend.domain.model.Region;
import com.example.PasteleriaBackend.domain.repository.CommuneRepository;
import com.example.PasteleriaBackend.domain.repository.RegionRepository;

@Component
@Order(25)
@Transactional
public class RegionSeeder implements ApplicationRunner {

    private final RegionRepository regionRepository;
    private final CommuneRepository communeRepository;

    public RegionSeeder(RegionRepository regionRepository, CommuneRepository communeRepository) {
        this.regionRepository = regionRepository;
        this.communeRepository = communeRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (regionRepository.count() > 0) {
            return;
        }

        createRegion("Metropolitana", List.of("Santiago", "Providencia", "Maipú", "La Florida"));
        createRegion("Valparaíso", List.of("Valparaíso", "Viña del Mar", "Concón", "Quilpué"));
        createRegion("Biobío", List.of("Concepción", "Coronel", "Los Ángeles", "Chiguayante"));
    }

    private void createRegion(String regionName, List<String> communes) {
        Region region = regionRepository.findByNameIgnoreCase(regionName)
            .orElseGet(() -> {
                Region created = new Region();
                created.setName(regionName);
                return regionRepository.save(created);
            });

        Set<String> existing = new HashSet<>();
        communeRepository.findByRegionId(region.getId()).forEach(commune -> existing.add(commune.getName().toLowerCase()));

        for (String communeName : communes) {
            if (existing.contains(communeName.toLowerCase())) {
                continue;
            }
            Commune commune = new Commune();
            commune.setRegion(region);
            commune.setName(communeName);
            communeRepository.save(commune);
        }
    }
}
