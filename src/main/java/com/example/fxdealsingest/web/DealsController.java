package com.example.fxdealsingest.web;

import com.example.fxdealsingest.dto.DealDTO;
import com.example.fxdealsingest.service.DealsService;
import com.example.fxdealsingest.service.ProcessingResult;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/deals")
public class DealsController {
    private static final Logger log = LoggerFactory.getLogger(DealsController.class);
    private final DealsService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ingestJson(@RequestBody List<DealDTO> requests) {
        List<ProcessingResult> results = service.processBatch(requests);
        return ResponseEntity.ok(results);
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("file empty");

        List<DealDTO> rows = new ArrayList<>();
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVFormat csvFormat = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreEmptyLines(true)
                    .withTrim();
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            for (CSVRecord record : records) {
                DealDTO dto = new DealDTO();
                dto.setDealUniqueId(record.get("Deal Unique Id"));
                dto.setFromCurrency(record.get("From Currency"));
                dto.setToCurrency(record.get("To Currency"));
                try {
                    dto.setDealTimestamp(OffsetDateTime.parse(record.get("Deal timestamp")));
                } catch (Exception e) {
                    dto.setDealTimestamp(null);
                }
                try {
                    dto.setAmount(Double.valueOf(record.get("Deal Amount")));
                } catch (Exception e) {
                    dto.setAmount(null);
                }
                rows.add(dto);
            }
        }
        List<ProcessingResult> results = service.processBatch(rows);
        return ResponseEntity.ok(results);
    }
}
