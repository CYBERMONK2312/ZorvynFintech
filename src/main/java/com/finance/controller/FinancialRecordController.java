package com.finance.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.finance.dto.RecordDto;
import com.finance.service.RecordService;

@RestController
@RequestMapping("/api/records")
public class FinancialRecordController {
    
    private final RecordService recordService =  null;

    // ADMIN only: Create records
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public RecordDto createRecord(@RequestBody RecordDto dto) {
        Long currentUserId = 1L; 
        return recordService.createRecord(currentUserId, dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST', 'VIEWER')")
    public org.springframework.data.domain.Page<RecordDto> getRecords(Pageable pageable) {
        Long currentUserId = 1L;
        return recordService.getRecordsForUser(currentUserId, pageable);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
    }
}