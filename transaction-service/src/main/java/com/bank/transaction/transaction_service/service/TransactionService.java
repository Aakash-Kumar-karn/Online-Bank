package com.bank.transaction.transaction_service.service;

import com.bank.transaction.transaction_service.dto.TransactionRequestDto;
import com.bank.transaction.transaction_service.dto.TransactionResponseDto;
import com.bank.transaction.transaction_service.model.Transaction;
import com.bank.transaction.transaction_service.service.impl.TransactionServiceImpl;

public interface TransactionService {
    TransactionResponseDto deposit(TransactionRequestDto dto, Long userId);
    Double getCurrentBalance(Long userId);

    TransactionResponseDto withdraw(TransactionRequestDto dto, Long userId);
    TransactionResponseDto shopViaNetBanking(TransactionRequestDto dto, Long userId);

    TransactionResponseDto shopViaCard(TransactionRequestDto dto, Long userId);

    void settleLoanTransaction(Long userId, Double amount); // from loan ms

}
