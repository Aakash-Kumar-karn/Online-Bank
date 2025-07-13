package com.bank.loan.loan_service.exception;

public class TokenMalformedException extends RuntimeException {
    public TokenMalformedException(String message) {
        super(message);
    }
}
