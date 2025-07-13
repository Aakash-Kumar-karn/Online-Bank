package com.bank.card.CardService.exception;

public class RequestAlreadyProcessedException extends RuntimeException{

    public RequestAlreadyProcessedException(String msg){
        super(msg);
    }

}
