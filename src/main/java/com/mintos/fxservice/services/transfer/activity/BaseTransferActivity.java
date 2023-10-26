package com.mintos.fxservice.services.transfer.activity;

import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.exceptions.InsufficientBalanceException;
import com.mintos.fxservice.exceptions.InvalidBalanceException;
import com.mintos.fxservice.exceptions.UnsupportedCurrencyException;
import com.mintos.fxservice.exceptions.UnsupportedTransferTypeException;
import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.repositories.DocumentRepository;
import com.mintos.fxservice.services.transaction.TransactionManager;
import com.mintos.fxservice.services.transfer.TransferType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public abstract class BaseTransferActivity implements TransferActivity {
    protected DocumentRepository documentRepository;
    protected TransactionManager transactionManager;
    protected TransferType transferType;
    @Override
    public TransferType getTransferType() {
        return transferType;
    }

    public BaseTransferActivity(DocumentRepository documentRepository, TransactionManager transactionManager) {
        this.documentRepository = documentRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public Document processTransfer(TransferDetails transferDetails) {
         validateTransferDetails(transferDetails);
         var documentBuilder = performTransferInternal(transferDetails);
         return finalizeAndBuildDocument(documentBuilder);
    }

    protected abstract Document.DocumentBuilder performTransferInternal(TransferDetails transferDetails);

    protected Document finalizeAndBuildDocument(Document.DocumentBuilder documentBuilder){
        documentBuilder.createdAt(LocalDateTime.now());
        var document = documentBuilder.build();
        documentRepository.save(document);
        transactionManager.createTransactions(document);
        return document;
    }

    protected void validateTransferDetails(TransferDetails transferDetails) {
        validateCurrencyCompatibility(transferDetails.getSourceAccount(), transferDetails.getTargetAccount(), transferDetails.getCcy());
        validateAccountEquality(transferDetails.getSourceAccount(), transferDetails.getTargetAccount());
        validateAmount(transferDetails.getAmount());
    }

    protected void validateCurrencyCompatibility(Account sourceAccount, Account targetAccount, String ccy) {
        if (!List.of(sourceAccount.getCcy(), targetAccount.getCcy()).contains(ccy)) {
            throw new UnsupportedCurrencyException("Unsupported currency: " + ccy);
        }
    }

    protected void validateAccountEquality(Account sourceAccount, Account targetAccount){
        if(sourceAccount.equals(targetAccount)){
            throw new UnsupportedTransferTypeException("Source and target accounts cannot be the same");
        }
    }

    protected void validateAmount(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new InvalidBalanceException("Invalid transfer amount. Value mast be positive.");
        }
    }

    protected void checkAccountBalance(Account account, BigDecimal amount){
        if (account.getBalance().compareTo(amount) < 0){
            throw new InsufficientBalanceException("Insufficient balance on account "  + account.getAccountNumber() + "for the transfer.");
        }
    }

    protected Document.DocumentBuilder createDocumentBuilder(TransferDetails transferDetails){
        return Document.builder()
                .documentNumber(UUID.randomUUID().toString())
                .transferType(transferDetails.getTransferType())
                .sourceAccount(transferDetails.getSourceAccount())
                .targetAccount(transferDetails.getTargetAccount())
                .sourceCcy(transferDetails.getSourceAccount().getCcy())
                .targetCcy(transferDetails.getTargetAccount().getCcy())
                .amount(transferDetails.getAmount())
                .ccy(transferDetails.getCcy());
    }
}

