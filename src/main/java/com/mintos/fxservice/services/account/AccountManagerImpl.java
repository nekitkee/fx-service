package com.mintos.fxservice.services.account;

import com.mintos.fxservice.dtos.account.AccountCreateRequest;
import com.mintos.fxservice.dtos.account.AccountDto;
import com.mintos.fxservice.exceptions.ClientNotFoundException;
import com.mintos.fxservice.exceptions.InvalidBalanceException;
import com.mintos.fxservice.exceptions.UnsupportedCurrencyException;
import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Client;
import com.mintos.fxservice.repositories.AccountRepository;
import com.mintos.fxservice.repositories.ClientRepository;
import com.mintos.fxservice.services.ccy.CurrencyManager;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountManagerImpl implements AccountManager {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final CurrencyManager currencyManager;
    private final Converter<Account, AccountDto> accountConverter;

    @Override
    public List<AccountDto> getAccountsByClientNumber(String clientNumber) {
        return accountRepository.findByClientNumber(clientNumber).stream()
              .map(accountConverter::convert).toList();
    }

    @Override
    public AccountDto createAccount(AccountCreateRequest request) {
        Client client = getClientByClientNumber(request.getClientNumber());
        checkSupportedCurrency(request.getCcy());
        validateBalance(request.getBalance());
        Account account = createAccountEntity(client, request);
        return accountConverter.convert(account);
    }

    private Client getClientByClientNumber(String clientNumber) {
        return clientRepository.findByClientNumber(clientNumber)
                .orElseThrow(() -> new ClientNotFoundException("Client not found with clientNumber: " + clientNumber));
    }

    private void checkSupportedCurrency(String currency) {
        if (!currencyManager.isCcyEnabled(currency)) {
            throw new UnsupportedCurrencyException("Unsupported currency: " + currency);
        }
    }

    private void validateBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBalanceException("Balance must be a non-negative value");
        }
    }

    private Account createAccountEntity(Client client, AccountCreateRequest request) {
        return accountRepository.save(Account.builder()
                .accountNumber(UUID.randomUUID().toString())
                .client(client)
                .ccy(request.getCcy())
                .balance(request.getBalance())
                .build());
    }
}
