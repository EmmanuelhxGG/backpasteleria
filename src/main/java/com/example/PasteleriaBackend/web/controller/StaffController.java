package com.example.PasteleriaBackend.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PasteleriaBackend.service.StaffService;
import com.example.PasteleriaBackend.web.dto.StaffRequest;
import com.example.PasteleriaBackend.web.dto.StaffResponse;
import com.example.PasteleriaBackend.web.dto.StaffUpdateRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/staff")
@PreAuthorize("hasRole('ADMIN')")
@Validated
@Tag(name = "Administración de personal")
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    @Operation(summary = "Crear un usuario administrativo")
    public ResponseEntity<StaffResponse> create(@Valid @RequestBody StaffRequest request) {
        return new ResponseEntity<>(staffService.createStaff(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar personal administrativo")
    public ResponseEntity<List<StaffResponse>> list() {
        return ResponseEntity.ok(staffService.listStaff());
    }

    @PutMapping("/{staffId}")
    @Operation(summary = "Actualizar datos de un colaborador")
    public ResponseEntity<StaffResponse> update(
        @PathVariable String staffId,
        @Valid @RequestBody StaffUpdateRequest request
    ) {
        return ResponseEntity.ok(staffService.updateStaff(staffId, request));
    }

    @DeleteMapping("/{staffId}")
    @Operation(summary = "Eliminar un colaborador")
    public ResponseEntity<Void> delete(@PathVariable String staffId) {
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }
}
