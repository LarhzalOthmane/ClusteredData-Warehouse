package com.example.fxdealsingest.service;

import java.util.List;

public record ProcessingResult(String dealUniqueId, String status, List<String> errors) {
    public static ProcessingResult success(String id) {
        return new ProcessingResult(id, "SUCCESS", List.of());
    }

    public static ProcessingResult skipped(String id, String reason) {
        return new ProcessingResult(id, "SKIPPED", List.of(reason));
    }

    public static ProcessingResult failure(String id, List<String> errors) {
        return new ProcessingResult(id, "FAILED", errors);
    }
}
