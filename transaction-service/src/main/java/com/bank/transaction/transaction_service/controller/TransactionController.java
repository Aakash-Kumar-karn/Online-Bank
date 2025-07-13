package com.bank.transaction.transaction_service.controller;

import com.bank.transaction.transaction_service.dto.TransactionRequestDto;
import com.bank.transaction.transaction_service.dto.TransactionResponseDto;
import com.bank.transaction.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


@PostMapping("/deposit")
public ResponseEntity<TransactionResponseDto> deposit(
        @Valid @RequestBody TransactionRequestDto dto,
        @RequestHeader("X-USER-ID") Long userId
){

    TransactionResponseDto response = transactionService.deposit(dto,userId);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
}

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(
            @RequestBody @Valid TransactionRequestDto dto,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        TransactionResponseDto result = transactionService.withdraw(dto, userId);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(@RequestHeader("X-USER-ID") Long userId) {
        Double balance = transactionService.getCurrentBalance(userId);
        return ResponseEntity.ok(balance);
    }
    @PostMapping("/shop/netbanking")
    public ResponseEntity<TransactionResponseDto> shopViaNetBanking(
            @RequestBody @Valid TransactionRequestDto dto,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        TransactionResponseDto response = transactionService.shopViaNetBanking(dto, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/shop/card")
    public ResponseEntity<TransactionResponseDto> shopViaCard(
            @RequestBody @Valid TransactionRequestDto dto,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return new ResponseEntity<>(transactionService.shopViaCard(dto, userId), HttpStatus.CREATED);
    }
    @PostMapping("/settle-loan")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> settleLoanTransaction(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam Double amount
    ) {
        transactionService.settleLoanTransaction(userId, amount);
        return new ResponseEntity<>("Loan amount deducted successfully. ",HttpStatus.OK);

    }


}
