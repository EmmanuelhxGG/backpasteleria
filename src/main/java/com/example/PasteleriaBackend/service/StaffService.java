package com.example.PasteleriaBackend.service;

import java.util.List;

import com.example.PasteleriaBackend.web.dto.StaffRequest;
import com.example.PasteleriaBackend.web.dto.StaffResponse;
import com.example.PasteleriaBackend.web.dto.StaffUpdateRequest;

public interface StaffService {

    StaffResponse createStaff(StaffRequest request);

    List<StaffResponse> listStaff();

    StaffResponse updateStaff(String staffId, StaffUpdateRequest request);

    void deleteStaff(String staffId);
}
