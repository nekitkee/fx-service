package com.mintos.fxservice.dtos.exchangerate;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangerateResponse {
    private boolean success;
    private String source;
    private long timestamp;
    private Map<String, BigDecimal> quotes;
    private ExchangerateError error;
}
