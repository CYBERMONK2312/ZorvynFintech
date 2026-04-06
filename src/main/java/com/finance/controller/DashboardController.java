package com.finance.controller;

import com.finance.dto.DashboardSummary;
import com.finance.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // ANALYST and ADMIN: View Insights
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public DashboardSummary getDashboardSummary() {
        Long currentUserId = 1L; // Extract from SecurityContext
        return dashboardService.getSummaryForUser(currentUserId);
    }
}