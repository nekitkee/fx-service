package com.mintos.fxservice.services.ccy;

import java.math.BigDecimal;

public interface CurrencyRateService {
    BigDecimal getExchangeRate(String sourceCcy, String targetCcy);
}
