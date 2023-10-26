package com.mintos.fxservice.exceptions;

import com.mintos.fxservice.dtos.exchangerate.ExchangerateError;

public class ExchangerateApiException extends RuntimeException {
    public ExchangerateApiException(String message) {
        super(message);
    }

    public ExchangerateApiException(ExchangerateError error){
        super(error.getInfo());
    }

    public ExchangerateApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
