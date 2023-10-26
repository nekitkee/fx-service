package com.mintos.fxservice.dtos.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotBlank(message = "Source account number should not be blank")
    private String sourceAccountNumber;

    @NotBlank(message = "Target account number should not be blank")
    private String targetAccountNumber;

    @NotBlank(message = "Currency should not be blank")
    private String ccy;

    @NotNull
    @Positive(message = "Amount should be a positive value")
    private BigDecimal amount;
}
