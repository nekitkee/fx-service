package com.mintos.fxservice.controller;

import com.mintos.fxservice.controllers.GlobalExceptionHandler;
import com.mintos.fxservice.dtos.Response;
import com.mintos.fxservice.exceptions.ClientNotFoundException;
import com.mintos.fxservice.exceptions.ExchangerateApiException;
import com.mintos.fxservice.exceptions.UnsupportedCurrencyException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void testHandleNotFoundException() {
        Exception e = new ClientNotFoundException("Client not found");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleNotFoundException(e);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Client not found", responseEntity.getBody().getMessage());
    }

    @Test
    public void testHandleBadRequestException() {
        Exception e = new UnsupportedCurrencyException("Unsupported currency");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleBadRequestException(e);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Unsupported currency", responseEntity.getBody().getMessage());
    }

    @Test
    public void testExchangerateApiException() {
        ExchangerateApiException e = new ExchangerateApiException("Exchange rate API error");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.ExchangerateApiException(e);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Exchange rate API error", responseEntity.getBody().getMessage());
    }


    @Test
    public void testHandleValidationException() {
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        MethodArgumentNotValidException methodArgumentNotValidException = Mockito.mock(MethodArgumentNotValidException.class);
        var errors = Collections.singletonList(new ObjectError("test","Validation error"));
        when(bindingResult.getAllErrors()).thenReturn(errors);
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleValidationException(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Validation error", responseEntity.getBody().getMessage());
        verify(bindingResult).getAllErrors();
    }

    @Test
    public void testHandleGenericException() {
        Exception e = new Exception("Generic error");

        ResponseEntity<Response> responseEntity = globalExceptionHandler.handleGenericException(e);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Internal Error", responseEntity.getBody().getMessage());
    }
}
