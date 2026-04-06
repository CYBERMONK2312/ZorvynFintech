package com.finance.repository;

import com.finance.domain.FinancialRecord;
import com.finance.domain.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, Long> {
    Page<FinancialRecord> findByUserId(Long userId, Pageable pageable);
    
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.user.id = :userId AND r.type = :type")
    BigDecimal sumAmountByUserIdAndType(@Param("userId") Long userId, @Param("type") RecordType type);

    @Query("SELECT r.category, SUM(r.amount) FROM FinancialRecord r WHERE r.user.id = :userId AND r.type = 'EXPENSE' GROUP BY r.category")
    List<Object[]> sumExpensesByCategory(@Param("userId") Long userId);
    
    @Query("SELECT r FROM FinancialRecord r WHERE r.user.id = :userId " +
            "AND (:type IS NULL OR r.type = :type) " +
            "AND (:category IS NULL OR r.category = :category)")
     Page<FinancialRecord> findFilteredRecords(
             @Param("userId") Long userId, 
             @Param("type") RecordType type, 
             @Param("category") String category, 
             Pageable pageable);
    
    
    List<FinancialRecord> findTop5ByUserIdOrderByRecordDateDesc(Long userId);
}