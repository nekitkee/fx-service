package com.mintos.fxservice.services.ccy;

import com.mintos.fxservice.api_clients.ExchagerateApiClient;
import com.mintos.fxservice.dtos.exchangerate.ExchangerateResponse;
import com.mintos.fxservice.exceptions.ExchangerateApiException;
import com.mintos.fxservice.exceptions.UnsupportedCurrencyException;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class ExchangerateService implements CurrencyRateService {

    private final ExchagerateApiClient exchagerateApiClient;

    /**
     *
     * If business logic allows using rates for a certain period of time (for example 1 hour or 1 day),
     * consider implementing one of the following strategies:
     *
     * 1. Scheduled Job to update Rates: Create a scheduled job that periodically pulls
     *    exchange rates from the Exchangerate and stores them in a db or cache.
     *    This way, our application can use the locally cached rates, reducing the need
     *    to call the external service on every request.
     *
     * 2. Circuit Breaker Pattern with Db Fallback: Implement the circuit breaker pattern.
     *    In case the service becomes unavailable, application can switch to a fallback strategy
     *    that uses stored rates.
     */

    @Override
    @Retry(name="exchangerate")
    public BigDecimal getExchangeRate(String sourceCcy, String targetCcy) {
        try {
            var response = exchagerateApiClient.getExchangeRates(sourceCcy);
            if (!response.isSuccess()) {
                throw new ExchangerateApiException(response.getError());
            }
            return retrieveExchangeRate(response, sourceCcy, targetCcy);
        }
        catch (ExchangerateApiException | UnsupportedCurrencyException e ) {
            throw e;
        }catch (Exception e){
            log.error("Error while fetching exchange rate" , e);
            throw new ExchangerateApiException("Error while fetching exchange rate", e);
        }
    }

    private BigDecimal retrieveExchangeRate(ExchangerateResponse response, String sourceCcy, String targetCcy){
        var rateKey = sourceCcy+targetCcy;
        var rate = response.getQuotes().get(rateKey);
        if (rate == null){
            throw new UnsupportedCurrencyException("Currency rate " + rateKey + " not found");
        }
        return rate;
    }
}
