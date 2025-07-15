package com.bank.insurance.insurance_service.exception;



public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String msg) {
        super(msg);
    }
}
