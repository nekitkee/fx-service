package com.mintos.fxservice.dtos.transfer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DocumentDto {
    private String documentNumber;
    private LocalDateTime createdAt;
    private String sourceAccountNumber;
    private String targetAccountNumber;
    private String ccy;
    private BigDecimal amount;
}
