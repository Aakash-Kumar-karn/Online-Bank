package com.bank.card.CardService.service.impl;

import com.bank.card.CardService.dto.CardDto;
import com.bank.card.CardService.dto.LimitUpgradeRequestDto;
import com.bank.card.CardService.dto.SetPinRequestDto;
import com.bank.card.CardService.exception.CardAlreadyBlockedException;
import com.bank.card.CardService.exception.CardLimitUpgradeException;
import com.bank.card.CardService.exception.CardNotFoundException;
import com.bank.card.CardService.exception.RequestAlreadyProcessedException;
import com.bank.card.CardService.model.Card;
import com.bank.card.CardService.model.CardLimitUpgradeRequest;
import com.bank.card.CardService.model.RequestStatus;
import com.bank.card.CardService.repository.CardLimitUpgradeRequestRepository;
import com.bank.card.CardService.repository.CardRepository;
import com.bank.card.CardService.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final CardRepository cardRepo ;
    @Autowired
    private CardLimitUpgradeRequestRepository upgradeRequestRepo;

    @Override
    public CardDto issueCard(CardDto cardDto) {

        Card card =new Card();

        card.setUserId(cardDto.getUserId());
        card.setCardType(cardDto.getCardType());
        card.setCardHolderName(cardDto.getCardHolderName());
        card.setCardLimit(cardDto.getCardLimit());

        //  Generate dummy card number
        card.setCardNumber(generateCardNumber());
        card.setExpiryDate(LocalDate.now().plusYears(3));

        //  PIN should be set later separately (null for now)
        card.setPin(null);
        card.setBlocked(false);

        Card savedCard = cardRepo.save(card);
        cardDto.setId(savedCard.getId()); //Sends that ID back to client
        return cardDto;
    }

    private String generateCardNumber() {
        Random nu = new Random();
        return "5522" + String.format("%012d", nu.nextLong(999999999999L));
    }
    @Override
    public void blockCard(Long cardId) {

        Card card = cardRepo.findById(cardId)
                .orElseThrow(()-> new CardNotFoundException("Card Not Found"));

        if(card.isBlocked()){
            throw new CardAlreadyBlockedException("Card is already blocked");
        }

        card.setBlocked(true);
        cardRepo.save(card);

    }

    @Override
    public void setCardPin(SetPinRequestDto dto) {
        Card card = cardRepo.findById(dto.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Card not found with ID: " + dto.getCardId()));

        if (card.isBlocked()) {
            throw new CardAlreadyBlockedException("Cannot set PIN for a blocked card.");
        }

        card.setPin(passwordEncoder.encode(dto.getPin()));
        cardRepo.save(card);
    }

    @Override
    public void requestCardLimitUpgrade(LimitUpgradeRequestDto dto) {
        Card card = cardRepo.findById(dto.getCardId())
                .orElseThrow(() -> new CardNotFoundException("Card not found with ID: " + dto.getCardId()));

        if (card.isBlocked()) {
            throw new CardAlreadyBlockedException("Cannot upgrade limit of a blocked card.");
        }

        CardLimitUpgradeRequest request = new CardLimitUpgradeRequest();
        request.setCardId(card.getId());
        request.setUserId(dto.getUserId());
        request.setCurrentLimit(card.getCardLimit());
        request.setRequestedLimit(dto.getRequestedLimit());
        request.setReason(dto.getReason());
        request.setRequestedAt(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);
        upgradeRequestRepo.save(request);

    }

    @Override
    public List<CardLimitUpgradeRequest> getAllUpgradeRequests() {

        return upgradeRequestRepo.findAll();
    }

    @Override
    public void processUpgradeRequest(Long requestId, boolean approve) {

        CardLimitUpgradeRequest request = upgradeRequestRepo.findById(requestId)
                .orElseThrow(()->new CardLimitUpgradeException("Upgrade req not found with ID: "+ requestId));

        if(request.getStatus() != RequestStatus.PENDING){
            throw new RequestAlreadyProcessedException("Request already processed");
        }

        if(approve){
            //  Approve and update card limit
            Card card = cardRepo.findById(request.getCardId())
                    .orElseThrow(()-> new CardNotFoundException("Card not found"));

            card.setCardLimit(request.getRequestedLimit());
            cardRepo.save(card);

            request.setStatus(RequestStatus.APPROVED);

        }else{
            request.setStatus(RequestStatus.REJECTED);
        }

        upgradeRequestRepo.save(request);
    }

    @Override
    public boolean isCardValid(Long cardId) {
        Card card = cardRepo.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        return !card.isBlocked();
    }

    @Override
    public String getCardType(Long cardId) {
        Card card = cardRepo.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        return card.getCardType().name(); // returns "DEBIT" or "CREDIT"
    }

}
