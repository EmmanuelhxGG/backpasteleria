package com.example.PasteleriaBackend.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PasteleriaBackend.service.RegionService;
import com.example.PasteleriaBackend.web.dto.CommuneResponse;
import com.example.PasteleriaBackend.web.dto.RegionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/regions")
@Tag(name = "Regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping
    @Operation(summary = "List regions", description = "Returns all regions available for shipping destinations")
    public ResponseEntity<List<RegionResponse>> getRegions() {
        return ResponseEntity.ok(regionService.findAll());
    }

    @GetMapping("/{regionId}/communes")
    @Operation(summary = "List communes by region", description = "Returns communes for a given region identifier")
    public ResponseEntity<List<CommuneResponse>> getCommunes(@PathVariable Long regionId) {
        return ResponseEntity.ok(regionService.findCommunesByRegion(regionId));
    }
}
