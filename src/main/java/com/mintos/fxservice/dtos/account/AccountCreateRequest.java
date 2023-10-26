package com.mintos.fxservice.dtos.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountCreateRequest {
    @NotNull(message = "Client number is required")
    private String clientNumber;
    @NotNull(message = "Currency is required")
    private String ccy;
    @NotNull(message = "Balance is required")
    @PositiveOrZero(message = "Balance must be a non-negative value")
    private BigDecimal balance;
}
