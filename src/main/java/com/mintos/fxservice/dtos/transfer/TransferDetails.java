package com.mintos.fxservice.dtos.transfer;

import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.services.transfer.TransferType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferDetails {
    private TransferType transferType;
    private Account sourceAccount;
    private Account targetAccount;
    private String ccy;
    private BigDecimal amount;

}
