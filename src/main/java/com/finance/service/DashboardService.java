package com.finance.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.finance.domain.RecordType;
import com.finance.dto.DashboardSummary;
import com.finance.dto.RecordDto;
import com.finance.repository.FinancialRecordRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final FinancialRecordRepository repository;

    public DashboardSummary getSummaryForUser(Long userId) {
        BigDecimal income = repository.sumAmountByUserIdAndType(userId, RecordType.INCOME);
        BigDecimal expense = repository.sumAmountByUserIdAndType(userId, RecordType.EXPENSE);
        BigDecimal net = income.subtract(expense);

        Map<String, BigDecimal> categoryData = repository.sumExpensesByCategory(userId).stream()
            .collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (BigDecimal) row[1]
            ));

        List<RecordDto> recentActivity = repository.findTop5ByUserIdOrderByRecordDateDesc(userId)
            .stream()
            .map(r -> new RecordDto(r.getId(), r.getAmount(), r.getType(), r.getCategory(), r.getRecordDate(), r.getNotes()))
            .toList();

        return new DashboardSummary(income, expense, net, categoryData, recentActivity);
    }
}