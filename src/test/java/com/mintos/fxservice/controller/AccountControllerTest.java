package com.mintos.fxservice.controller;


import com.mintos.fxservice.controllers.AccountController;
import com.mintos.fxservice.dtos.account.AccountCreateRequest;
import com.mintos.fxservice.dtos.account.AccountDto;
import com.mintos.fxservice.exceptions.UnsupportedCurrencyException;
import com.mintos.fxservice.services.account.AccountManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;
    @Mock
    private AccountManager accountManager;

    @Test
    public void testGetAccountsByClientNumber_successful() {
        List<AccountDto> accountDtos = new ArrayList<>();
        AccountDto accountDto = new AccountDto();
        accountDto.setAccountNumber("clientNumber");
        accountDto.setBalance(BigDecimal.ZERO);
        accountDto.setCcy("USD");
        accountDtos.add(accountDto);
        Mockito.when(accountManager.getAccountsByClientNumber("clientNumber")).thenReturn(accountDtos);

        var response = accountController.getAccountsByClientNumber("clientNumber");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void createAccount_successful() {

        AccountCreateRequest request = new AccountCreateRequest();
        AccountDto createdAccountDto = new AccountDto();
        Mockito.when(accountManager.createAccount(request)).thenReturn(createdAccountDto);

        var response = accountController.createAccount(request);

        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void createAccount_unsupportedCurrency_failure() {
        AccountCreateRequest request = new AccountCreateRequest();
        Mockito.when(accountManager.createAccount(request)).thenThrow(UnsupportedCurrencyException.class);

        assertThrows(UnsupportedCurrencyException.class, () -> accountController.createAccount(request));
    }

}
