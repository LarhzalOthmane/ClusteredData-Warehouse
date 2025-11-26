// file: src/test/java/com/example/fxdealsingest/service/DealServiceTest.java
package com.example.fxdealsingest.service;

import com.example.fxdealsingest.dto.DealDTO;
import com.example.fxdealsingest.entities.Deal;
import com.example.fxdealsingest.repositories.DealsRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class DealServiceTest {
    @Mock
    DealsRepository repository;
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    DealsService service;
    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new DealsService(repository, validator);
    }

    @Test
    void testSaveSuccess() {
        DealDTO request = new DealDTO();
        request.setDealUniqueId(uuid.toString());
        request.setFromCurrency("USD");
        request.setToCurrency("EUR");
        request.setDealTimestamp(OffsetDateTime.now());
        request.setAmount(1000.0);

        when(repository.existsByDealUniqueId(uuid.toString())).thenReturn(false);
        when(repository.save(any(Deal.class))).thenReturn(new Deal());

        ProcessingResult result = service.processSingle(request);
        assertEquals("SUCCESS", result.status());
    }

    @Test
    void testDuplicateSkipped() {
        DealDTO request = new DealDTO();
        request.setDealUniqueId(uuid.toString());
        request.setFromCurrency("USD");
        request.setToCurrency("EUR");
        request.setDealTimestamp(OffsetDateTime.now());
        request.setAmount(1000.0);

        when(repository.existsByDealUniqueId(uuid.toString())).thenReturn(true);

        ProcessingResult result = service.processSingle(request);
        assertEquals("SKIPPED", result.status());
    }
}
