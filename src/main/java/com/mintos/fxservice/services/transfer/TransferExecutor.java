package com.mintos.fxservice.services.transfer;

import com.mintos.fxservice.dtos.transfer.DocumentDto;
import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.dtos.transfer.TransferRequest;
import com.mintos.fxservice.exceptions.AccountNotFoundException;
import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.repositories.AccountRepository;
import com.mintos.fxservice.services.transfer.activity.*;
import com.mintos.fxservice.services.transfer.factory.ActivityFactory;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class TransferExecutor implements TransferService {
    private final AccountRepository accountRepository;
    private final ActivityFactory activityFactory;
    private final Converter<Document, DocumentDto> dtoConverter;

    public DocumentDto transferFunds(TransferRequest transferRequest) {
        TransferDetails transferDetails = prepareTransferDetails(transferRequest);
        TransferActivity transferActivity = activityFactory.getTransferActivity(transferDetails);
        Document document = transferActivity.processTransfer(transferDetails);
        return dtoConverter.convert(document);
    }

    private TransferDetails prepareTransferDetails(TransferRequest transferRequest) {
        Account sourceAccount = findAccountByAccountNumber(transferRequest.getSourceAccountNumber());
        Account targetAccount = findAccountByAccountNumber(transferRequest.getTargetAccountNumber());
        TransferType transferType = determineTransferType(sourceAccount, targetAccount);
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setAmount(transferRequest.getAmount());
        transferDetails.setCcy(transferRequest.getCcy());
        transferDetails.setSourceAccount(sourceAccount);
        transferDetails.setTargetAccount(targetAccount);
        transferDetails.setTransferType(transferType);
        return transferDetails;
    }

    private Account findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountNumber));
    }

    private TransferType determineTransferType(Account sourceAccount, Account targetAccount) {
       if (Objects.equals(targetAccount.getCcy(), sourceAccount.getCcy())) {
            return TransferType.FIAT_SINGLE_CCY;
        } else {
            return TransferType.FIAT_MULTI_CCY;
        }
    }
}
