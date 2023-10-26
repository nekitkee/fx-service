package com.mintos.fxservice.dtos.account;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto {
    private String accountNumber;
    private String ccy;
    private BigDecimal balance;
}
