package com.example.fxdealsingest.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "deals", uniqueConstraints = @UniqueConstraint(name = "uq_deal_unique_id", columnNames = "deal_unique_id"))
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Deal {
    
    @Id
    @Column(name = "deal_unique_id", nullable = false, unique = true)
    private String dealUniqueId;

    @Column(name = "from_currency", length = 3, nullable = false)
    private String fromCurrency;

    @Column(name = "to_currency", length = 3, nullable = false)
    private String toCurrency;

    @Column(name = "deal_timestamp", nullable = false)
    private OffsetDateTime dealTimestamp;

    @Column(name = "amount", nullable = false)
    private double amount;

}
