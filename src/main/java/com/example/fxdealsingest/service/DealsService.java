// file: src/main/java/com/example/fxdeals/service/DealService.java
package com.example.fxdealsingest.service;

import com.example.fxdealsingest.dto.DealDTO;
import com.example.fxdealsingest.entities.Deal;
import com.example.fxdealsingest.repositories.DealsRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class DealsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DealsService.class);
    private final DealsRepository repository;
    private final Validator validator;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ProcessingResult processSingle(DealDTO request) {
        Set<ConstraintViolation<DealDTO>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            List<String> errors = new ArrayList<>();
            violations.forEach(violation -> errors.add(
                    violation.getPropertyPath() + " : " + violation.getMessage()
            ));
            LOGGER.warn("Validation failed for {}: {}", request.getDealUniqueId(), errors);
            return ProcessingResult.failure(request.getDealUniqueId(), errors);
        }

        if (repository.existsByDealUniqueId(request.getDealUniqueId())) {
            LOGGER.info("Duplicate detected, skipping dealUniqueId={}", request.getDealUniqueId());
            return ProcessingResult.skipped(request.getDealUniqueId(), "duplicate");
        }

        Deal deal = Deal.builder()
                .dealTimestamp(request.getDealTimestamp())
                .amount(request.getAmount())
                .fromCurrency(request.getFromCurrency())
                .toCurrency(request.getToCurrency())
                .dealUniqueId(request.getDealUniqueId())
                .build();
        try {
            repository.save(deal);
            LOGGER.info("Saved deal {}", request.getDealUniqueId());
            return ProcessingResult.success(request.getDealUniqueId());
        } catch (DataIntegrityViolationException ex) {
            LOGGER.warn("Integrity issue saving {}: {}", request.getDealUniqueId(), ex.getMessage());
            return ProcessingResult.skipped(request.getDealUniqueId(), "integrity");
        } catch (Exception ex) {
            LOGGER.error("Unexpected error saving {}: {}", request.getDealUniqueId(), ex.getMessage(), ex);
            return ProcessingResult.failure(request.getDealUniqueId(), List.of(ex.getMessage()));
        }
    }

    public List<ProcessingResult> processBatch(List<DealDTO> requests) {
        List<ProcessingResult> results = new ArrayList<>();
        for (DealDTO request : requests) {
            results.add(processSingle(request));
        }
        return results;
    }

}
