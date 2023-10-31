package com.mintos.fxservice.controllers;

import com.mintos.fxservice.dtos.account.AccountCreateRequest;
import com.mintos.fxservice.dtos.account.AccountDto;
import com.mintos.fxservice.services.account.AccountManager;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AccountController {
    private final AccountManager accountManager;

    @GetMapping("/clients/{clientNumber}/accounts")
    public ResponseEntity<List<AccountDto>> getAccountsByClientNumber(@PathVariable String clientNumber) {
        var accountList = accountManager.getAccountsByClientNumber(clientNumber);
        return new ResponseEntity<>(accountList, HttpStatus.OK);
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        AccountDto createdAccount = accountManager.createAccount(request);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }
}
