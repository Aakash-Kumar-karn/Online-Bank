package com.bank.card.CardService.repository;

import com.bank.card.CardService.model.CardLimitUpgradeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardLimitUpgradeRequestRepository extends JpaRepository<CardLimitUpgradeRequest, Long> {
}
