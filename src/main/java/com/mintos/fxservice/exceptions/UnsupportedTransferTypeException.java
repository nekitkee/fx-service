package com.mintos.fxservice.exceptions;

public class UnsupportedTransferTypeException extends IllegalArgumentException{
    public UnsupportedTransferTypeException() {
        super("Unsupported transfer type");
    }

    public UnsupportedTransferTypeException(String message) {
        super(message);
    }
}