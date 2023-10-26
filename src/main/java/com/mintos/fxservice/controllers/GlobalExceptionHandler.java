package com.mintos.fxservice.controllers;

import com.mintos.fxservice.dtos.Response;
import com.mintos.fxservice.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({ClientNotFoundException.class, AccountNotFoundException.class})
    public ResponseEntity<Response> handleNotFoundException(Exception e){
        Response response = new Response(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({InsufficientBalanceException.class, InvalidBalanceException.class,
            UnsupportedCurrencyException.class, UnsupportedTransferTypeException.class, ConstraintViolationException.class })
    public ResponseEntity<Response> handleBadRequestException(Exception e){
        Response response = new Response(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ExchangerateApiException.class})
    public ResponseEntity<Response> ExchangerateApiException(ExchangerateApiException e){
        Response response = new Response(e.getMessage());
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
        String errorMessages = String.join(", ", errors);
        Response response = new Response(errorMessages);
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGenericException(Exception e) {
        Response response = new Response("Internal Error");
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
