package com.mintos.fxservice.exceptions;

public class UnsupportedCurrencyException extends IllegalArgumentException{
    public UnsupportedCurrencyException(String message) {
        super(message);
    }
}
