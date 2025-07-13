package com.bank.card.CardService.controller;


import com.bank.card.CardService.dto.CardDto;
import com.bank.card.CardService.dto.LimitUpgradeRequestDto;
import com.bank.card.CardService.dto.SetPinRequestDto;
import com.bank.card.CardService.exception.CardNotFoundException;
import com.bank.card.CardService.model.Card;
import com.bank.card.CardService.model.CardLimitUpgradeRequest;
import com.bank.card.CardService.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/issue")
    public ResponseEntity<CardDto> issueCard(@Valid @RequestBody CardDto cardDto){
        CardDto issuedCard = cardService.issueCard(cardDto);
        return new ResponseEntity<>(issuedCard, HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/block/{cardId}")
    public ResponseEntity<String> blockCard(@PathVariable Long cardId) {
        cardService.blockCard(cardId); // assumed void
        return new ResponseEntity<>("Card blocked successfully", HttpStatus.ACCEPTED);
    }
// If a PIN doesn’t exist ->  it sets it.
// If a PIN already exists → it replaces it with new one.
// for update/set pin
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/set-pin")
    public ResponseEntity<String> setCardPin(@Valid @RequestBody SetPinRequestDto setPinRequestDto) {
        cardService.setCardPin(setPinRequestDto);
       return new ResponseEntity<>("PIN set successfully",HttpStatus.CREATED);
       }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/upgrade-limit")
    public ResponseEntity<String> upgradeCardLimit(@Valid @RequestBody LimitUpgradeRequestDto dto) {
        cardService.requestCardLimitUpgrade(dto);
        return ResponseEntity.ok("Card limit upgrade request submitted successfully.");
    }

    @GetMapping("/upgrade-requests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CardLimitUpgradeRequest>> getAllUpgradeRequests(){
       List<CardLimitUpgradeRequest> list = cardService.getAllUpgradeRequests();
      return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @PutMapping("/upgrade-requests/{requestId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> processUpgradeRequest(
            @PathVariable Long requestId ,
            @RequestParam boolean approve){
         cardService.processUpgradeRequest(requestId,approve);
        return new ResponseEntity<>("Request " + (approve ? "approved" : "rejected") + " successfully.",HttpStatus.OK);
    }

    @GetMapping("/is-valid/{cardId}")
    public ResponseEntity<Boolean> isCardValid(@PathVariable Long cardId) {
        boolean isValid = cardService.isCardValid(cardId);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/type/{cardId}")
    public ResponseEntity<String> getCardType(@PathVariable Long cardId) {
        String type = cardService.getCardType(cardId);
        return ResponseEntity.ok(type);
    }



}
