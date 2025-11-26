package com.example.fxdealsingest.repositories;

import com.example.fxdealsingest.entities.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DealsRepository extends JpaRepository<Deal, UUID> {
    boolean existsByDealUniqueId(String dealUniqueId);
}
