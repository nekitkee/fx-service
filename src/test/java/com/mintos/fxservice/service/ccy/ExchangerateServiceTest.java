package com.mintos.fxservice.service.ccy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.mintos.fxservice.api_clients.ExchagerateApiClient;
import com.mintos.fxservice.dtos.exchangerate.ExchangerateError;
import com.mintos.fxservice.dtos.exchangerate.ExchangerateResponse;
import com.mintos.fxservice.exceptions.ExchangerateApiException;
import com.mintos.fxservice.exceptions.UnsupportedCurrencyException;
import com.mintos.fxservice.services.ccy.ExchangerateService;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ExchangerateServiceTest {

    @Mock
    private ExchagerateApiClient exchagerateApiClient;
    @InjectMocks
    private ExchangerateService exchangerateService;

    @Test
    public void testGetExchangeRate_Success() {
        String sourceCcy = "USD";
        String targetCcy = "EUR";
        ExchangerateResponse response = new ExchangerateResponse();
        response.setSuccess(true);
        BigDecimal rate = BigDecimal.valueOf(1.12);
        response.setQuotes(Map.of("USDEUR", rate ));

        when(exchagerateApiClient.getExchangeRates(sourceCcy)).thenReturn(response);

        BigDecimal exchangeRate = exchangerateService.getExchangeRate(sourceCcy, targetCcy);

        assertEquals(rate, exchangeRate);
    }

    @Test
    public void testGetExchangeRate_ccyPairNotFound_failure() {
        String sourceCcy = "USD";
        String targetCcy = "EUR";
        ExchangerateResponse response = new ExchangerateResponse();
        response.setSuccess(true);
        response.setQuotes(Map.of());

        when(exchagerateApiClient.getExchangeRates(sourceCcy)).thenReturn(response);

        assertThrows(UnsupportedCurrencyException.class, () -> exchangerateService.getExchangeRate(sourceCcy, targetCcy));
    }

    @Test
    public void testGetExchangeRate_unsuccessfulResponse_failure() {
        String sourceCcy = "USD";
        String targetCcy = "EUR";
        ExchangerateError error = new ExchangerateError();
        error.setInfo("Some error message");
        ExchangerateResponse response = new ExchangerateResponse();
        response.setSuccess(false);
        response.setError(error);

        when(exchagerateApiClient.getExchangeRates(sourceCcy)).thenReturn(response);

        assertThrows(ExchangerateApiException.class, () -> exchangerateService.getExchangeRate(sourceCcy, targetCcy));
    }

    @Test
    public void testGetExchangeRate_clientThrows_failure() {
        String sourceCcy = "USD";
        String targetCcy = "EUR";

        when(exchagerateApiClient.getExchangeRates(sourceCcy)).thenThrow(FeignException.class);

        assertThrows(ExchangerateApiException.class, () -> exchangerateService.getExchangeRate(sourceCcy, targetCcy));
    }




}

