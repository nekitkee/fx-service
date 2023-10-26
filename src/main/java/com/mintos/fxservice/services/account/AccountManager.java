package com.mintos.fxservice.services.account;

import com.mintos.fxservice.dtos.account.AccountCreateRequest;
import com.mintos.fxservice.dtos.account.AccountDto;

import java.util.List;

public interface AccountManager {
    List<AccountDto> getAccountsByClientNumber(String clientNumber);

    AccountDto createAccount(AccountCreateRequest accountCreateRequest);
}
