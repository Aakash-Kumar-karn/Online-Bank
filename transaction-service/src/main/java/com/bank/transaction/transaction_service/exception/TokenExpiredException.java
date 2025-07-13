package com.bank.transaction.transaction_service.exception;



public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String msg) {
        super(msg);
    }
}
