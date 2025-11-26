package com.example.fxdealsingest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealDTO {
    @NotBlank
    private String dealUniqueId;

    @NotBlank
    @Size(min = 3, max = 3)
    private String fromCurrency;

    @NotBlank
    @Size(min = 3, max = 3)
    private String toCurrency;

    @NotNull
    private OffsetDateTime dealTimestamp;

    @NotNull
    private Double amount;

}
