package com.bank.transaction.transaction_service.exception;

public class TokenMalformedException extends RuntimeException {
    public TokenMalformedException(String message) {
        super(message);
    }
}
