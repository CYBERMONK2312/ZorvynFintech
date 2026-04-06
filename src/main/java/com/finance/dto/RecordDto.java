package com.finance.dto;

import com.finance.domain.RecordType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record RecordDto(
    Long id,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotNull RecordType type,
    @NotBlank String category,
    @NotNull LocalDate recordDate,
    String notes
) {}