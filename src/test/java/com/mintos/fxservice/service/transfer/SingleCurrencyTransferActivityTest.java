package com.mintos.fxservice.service.transfer;

import com.mintos.fxservice.exceptions.*;
import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.repositories.DocumentRepository;
import com.mintos.fxservice.services.transaction.TransactionManager;
import com.mintos.fxservice.services.transfer.activity.SingleCurrencyTransferActivity;
import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.services.transfer.TransferType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SingleCurrencyTransferActivityTest {

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private TransactionManager transactionManager;
    @InjectMocks
    private SingleCurrencyTransferActivity singleCurrencyTransferActivity;

    private static TransferDetails getTransferDetails(String ccy, BigDecimal amount, Account sourceAccount, Account targetAccount) {
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setTransferType(TransferType.FIAT_SINGLE_CCY);
        transferDetails.setCcy(ccy);
        transferDetails.setAmount(amount);
        transferDetails.setSourceAccount(sourceAccount);
        transferDetails.setTargetAccount(targetAccount);
        return transferDetails;
    }

    private static Account getAccount(String accountNumber, String ccy, BigDecimal amount) {
        var account = new Account();
        account.setAccountNumber(accountNumber);
        account.setCcy(ccy);
        account.setBalance(amount);
        return account;
    }

    @Test
    void testProcessTransfer_successful() {

        var ccy = "EUR";
        var amount = BigDecimal.TEN;
        Account sourceAccount = getAccount("sourceAccountNumber", ccy, amount );
        Account targetAccount = getAccount("targetAccountNumber", ccy, BigDecimal.ZERO);
        TransferDetails transferDetails = getTransferDetails(ccy, amount, sourceAccount, targetAccount);

        Document document = singleCurrencyTransferActivity.processTransfer(transferDetails);

        verify(documentRepository).save(document);
        verify(transactionManager).createTransactions(document);

    }

    @Test
    void testProcessTransfer_currencyIncompatible_failure() {

        var transferCcy = "RUB";
        var accountCcy = "EUR";
        var amount = BigDecimal.TEN;
        Account sourceAccount = getAccount("sourceAccountNumber", accountCcy, amount );
        Account targetAccount = getAccount("targetAccountNumber", accountCcy, BigDecimal.ZERO);
        TransferDetails transferDetails = getTransferDetails(transferCcy, amount, sourceAccount, targetAccount);

        assertThrows(UnsupportedCurrencyException.class, () -> singleCurrencyTransferActivity.processTransfer(transferDetails));

        verifyNoInteractions(documentRepository);
        verifyNoInteractions(transactionManager);

    }

    @Test
    void testProcessTransfer_equalAccount_failure() {

        var ccy = "EUR";
        var amount = BigDecimal.TEN;
        Account sourceAccount = getAccount("sourceAccountNumber", ccy, amount );
        TransferDetails transferDetails = getTransferDetails(ccy, amount, sourceAccount, sourceAccount);

        assertThrows(UnsupportedTransferTypeException.class, () -> singleCurrencyTransferActivity.processTransfer(transferDetails));

        verifyNoInteractions(documentRepository);
        verifyNoInteractions(transactionManager);

    }

    @Test
    void testProcessTransfer_negativeAmount_failure() {

        var ccy = "EUR";
        var amount = BigDecimal.TEN;
        var negativeTransferAmount = BigDecimal.TEN.negate();
        Account sourceAccount = getAccount("sourceAccountNumber", ccy, amount );
        Account targetAccount = getAccount("targetAccountNumber", ccy, BigDecimal.ZERO);
        TransferDetails transferDetails = getTransferDetails(ccy, negativeTransferAmount, sourceAccount, targetAccount);

        assertThrows(InvalidBalanceException.class, () -> singleCurrencyTransferActivity.processTransfer(transferDetails));

        verifyNoInteractions(documentRepository);
        verifyNoInteractions(transactionManager);

    }

    @Test
    void testProcessTransfer_insufficientFunds_failure() {

        var ccy = "EUR";
        var amount = BigDecimal.TEN;
        var bigTransferAmount = BigDecimal.valueOf(999999999);
        Account sourceAccount = getAccount("sourceAccountNumber", ccy, amount );
        Account targetAccount = getAccount("targetAccountNumber", ccy, BigDecimal.ZERO);
        TransferDetails transferDetails = getTransferDetails(ccy, bigTransferAmount, sourceAccount, targetAccount);

        assertThrows(InsufficientBalanceException.class, () -> singleCurrencyTransferActivity.processTransfer(transferDetails));

        verifyNoInteractions(documentRepository);
        verifyNoInteractions(transactionManager);

    }
}
