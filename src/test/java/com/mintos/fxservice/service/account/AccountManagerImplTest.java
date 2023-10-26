package com.mintos.fxservice.service.account;

import com.mintos.fxservice.dtos.account.AccountCreateRequest;
import com.mintos.fxservice.dtos.account.AccountDto;
import com.mintos.fxservice.exceptions.ClientNotFoundException;
import com.mintos.fxservice.exceptions.InvalidBalanceException;
import com.mintos.fxservice.exceptions.UnsupportedCurrencyException;
import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Client;
import com.mintos.fxservice.repositories.AccountRepository;
import com.mintos.fxservice.repositories.ClientRepository;
import com.mintos.fxservice.services.account.AccountManagerImpl;
import com.mintos.fxservice.services.ccy.CurrencyManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountManagerImplTest {

    @InjectMocks
    private AccountManagerImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private CurrencyManager currencyManager;
    @Mock
    private Converter<Account, AccountDto> accountConverter;

    @Test
    public void testGetAccountsByClientNumber_oneAccount_successful() {
        String clientNumber = "CLIENT-WITH-ONE-ACCOUNT";
        var account = new Account();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountRepository.findByClientNumber(clientNumber)).thenReturn(accounts);
        var accountDto = new AccountDto();
        when(accountConverter.convert(Mockito.any())).thenReturn(accountDto);

        List<AccountDto> result = accountService.getAccountsByClientNumber(clientNumber);

        assertNotNull(result);
    }

    @Test
    public void testGetAccountsByClientNumber_noAccount_successful() {
        String clientNumber = "CLIENT-WITHOUT-ACCOUNTS";
        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findByClientNumber(clientNumber)).thenReturn(accounts);

        List<AccountDto> result = accountService.getAccountsByClientNumber(clientNumber);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testCreateAccount_successful() {
        String clientNumber = "CLIENT2";
        String ccy = "USD";
        BigDecimal balance = BigDecimal.TEN;
        Client client = new Client();
        AccountCreateRequest request = new AccountCreateRequest();
        request.setBalance(balance);
        request.setClientNumber(clientNumber);
        request.setCcy(ccy);
        Account account = new Account();
        var accountDto = new AccountDto();

        when(accountRepository.save(Mockito.any())).thenReturn(account);
        when(clientRepository.findByClientNumber(clientNumber)).thenReturn(Optional.of(client));
        when(currencyManager.isCcyEnabled(ccy)).thenReturn(true);
        when(accountConverter.convert(account)).thenReturn(accountDto);

        AccountDto result = accountService.createAccount(request);

        assertNotNull(result);
    }

    @Test
    public void testCreateAccount_negativeBalance_Failed() {
        String clientNumber = "CLIENT2";
        String ccy = "USD";
        BigDecimal balance = BigDecimal.TEN.negate();
        Client client = new Client();
        AccountCreateRequest request = new AccountCreateRequest();
        request.setBalance(balance);
        request.setClientNumber(clientNumber);
        request.setCcy(ccy);
        when(currencyManager.isCcyEnabled(ccy)).thenReturn(true);
        when(clientRepository.findByClientNumber(clientNumber)).thenReturn(Optional.of(client));

        assertThrows(InvalidBalanceException.class, () -> accountService.createAccount(request));

        verifyNoInteractions(accountRepository);
    }

    @Test
    public void testCreateAccount_unsupportedCcy_Failed() {
        String clientNumber = "CLIENT2";
        BigDecimal balance = BigDecimal.TEN;
        String ccy = "LVA";
        AccountCreateRequest request = new AccountCreateRequest();
        request.setBalance(balance);
        request.setClientNumber(clientNumber);
        request.setCcy(ccy);
        Client client = new Client();
        when(clientRepository.findByClientNumber(clientNumber)).thenReturn(Optional.of(client));
        when(currencyManager.isCcyEnabled(ccy)).thenReturn(false);

        assertThrows(UnsupportedCurrencyException.class, () -> accountService.createAccount(request));

        verifyNoInteractions(accountRepository);
        verify(currencyManager, Mockito.times(1)).isCcyEnabled(ccy);
    }

    @Test
    public void testCreateAccount_clientNotFound_Failed() {
        String clientNumber = "CLIENT2";
        BigDecimal balance = BigDecimal.TEN;
        String ccy = "LVA";
        AccountCreateRequest request = new AccountCreateRequest();
        request.setBalance(balance);
        request.setClientNumber(clientNumber);
        request.setCcy(ccy);
        when(clientRepository.findByClientNumber(clientNumber)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> accountService.createAccount(request));

        verifyNoInteractions(accountRepository);
        verifyNoInteractions(currencyManager);
        verifyNoInteractions(accountConverter);
    }


}

