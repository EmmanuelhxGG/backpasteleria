package com.example.PasteleriaBackend.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.PasteleriaBackend.domain.repository.CommuneRepository;
import com.example.PasteleriaBackend.domain.repository.RegionRepository;
import com.example.PasteleriaBackend.service.RegionService;
import com.example.PasteleriaBackend.web.dto.CommuneResponse;
import com.example.PasteleriaBackend.web.dto.RegionResponse;

@Service
@Transactional(readOnly = true)
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final CommuneRepository communeRepository;

    public RegionServiceImpl(RegionRepository regionRepository, CommuneRepository communeRepository) {
        this.regionRepository = regionRepository;
        this.communeRepository = communeRepository;
    }

    @Override
    public List<RegionResponse> findAll() {
        return regionRepository.findAll().stream()
            .map(region -> new RegionResponse(region.getId(), region.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public List<CommuneResponse> findCommunesByRegion(Long regionId) {
        return communeRepository.findByRegionId(regionId).stream()
            .map(commune -> new CommuneResponse(commune.getId(), commune.getName()))
            .collect(Collectors.toList());
    }
}
