package com.finance.service;

import com.finance.domain.FinancialRecord;
import com.finance.domain.User;
import com.finance.dto.RecordDto;
import com.finance.repository.FinancialRecordRepository;
import com.finance.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecordService {

    private final FinancialRecordRepository recordRepository;
    private final UserRepository userRepository;

    @Transactional
    public RecordDto createRecord(Long userId, RecordDto dto) {
        // 1. Fetch the user to associate with the record
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // 2. Map DTO to Entity using the Builder pattern
        FinancialRecord record = FinancialRecord.builder()
                .user(user)
                .amount(dto.amount())
                .type(dto.type())
                .category(dto.category())
                .recordDate(dto.recordDate())
                .notes(dto.notes())
                .build();

        // 3. Save to database
        FinancialRecord savedRecord = recordRepository.save(record);

        // 4. Return the mapped DTO
        return mapToDto(savedRecord);
    }

    @Transactional(readOnly = true)
    public Page<RecordDto> getRecordsForUser(Long userId, Pageable pageable) {
        // Fetch paginated records and map them directly to DTOs
        return recordRepository.findByUserId(userId, pageable)
                .map(this::mapToDto);
    }

    @Transactional
    public void deleteRecord(Long id) {
        // Ensure record exists before attempting to delete
        if (!recordRepository.existsById(id)) {
            throw new EntityNotFoundException("Financial record not found with ID: " + id);
        }
        recordRepository.deleteById(id);
    }

    // Helper method to map Entity back to DTO
    private RecordDto mapToDto(FinancialRecord record) {
        return new RecordDto(
                record.getId(),
                record.getAmount(),
                record.getType(),
                record.getCategory(),
                record.getRecordDate(),
                record.getNotes()
        );
    }
}