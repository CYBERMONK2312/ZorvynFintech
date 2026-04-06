package com.finance.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record DashboardSummary(
    BigDecimal totalIncome,
    BigDecimal totalExpense,
    BigDecimal netBalance,
    Map<String, BigDecimal> expensesByCategory,
    List<RecordDto> recentActivity
) {}