package com.bank.card.CardService.service;


import com.bank.card.CardService.dto.CardDto;
import com.bank.card.CardService.dto.LimitUpgradeRequestDto;
import com.bank.card.CardService.dto.SetPinRequestDto;
import com.bank.card.CardService.model.CardLimitUpgradeRequest;

import java.util.List;
import java.util.Optional;

public interface CardService {
    CardDto issueCard(CardDto cardDto);
    void blockCard(Long cardId);
    void setCardPin(SetPinRequestDto pinRequestDto);
    void requestCardLimitUpgrade(LimitUpgradeRequestDto dto);
    List<CardLimitUpgradeRequest> getAllUpgradeRequests();
    void processUpgradeRequest(Long requestId, boolean approve);
    boolean isCardValid(Long cardId);  //another ms
    String getCardType(Long cardId); //another ms
}
