package com.bank.card.CardService.repository;

import com.bank.card.CardService.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CardRepository extends JpaRepository<Card,Long> {
   List<Card> findByUserId(Long userId);

}
