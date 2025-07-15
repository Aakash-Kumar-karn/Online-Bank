package com.bank.insurance.insurance_service.exception;

public class TokenMalformedException extends RuntimeException {
    public TokenMalformedException(String message) {
        super(message);
    }
}
