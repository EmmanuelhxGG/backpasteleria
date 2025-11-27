package com.example.PasteleriaBackend.service;

import java.util.List;

import com.example.PasteleriaBackend.web.dto.CommuneResponse;
import com.example.PasteleriaBackend.web.dto.RegionResponse;

public interface RegionService {

    List<RegionResponse> findAll();

    List<CommuneResponse> findCommunesByRegion(Long regionId);
}
