package com.mintos.fxservice.service.transaction;

import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.models.Transaction;
import com.mintos.fxservice.repositories.AccountRepository;
import com.mintos.fxservice.repositories.TransactionRepository;
import com.mintos.fxservice.services.transaction.TransactionManagerImpl;
import com.mintos.fxservice.services.transfer.TransferType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class TransactionManagerImplTest {

    @InjectMocks
    private TransactionManagerImpl transactionManager;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Test
    public void testCreateTransactions_sameCurrency_success() {

        BigDecimal transferAmount = BigDecimal.TEN;

        BigDecimal sourceAccountStartAmount = BigDecimal.TEN;
        BigDecimal sourceAccountExpectedAmount = sourceAccountStartAmount.subtract(transferAmount);
        Account sourceAccount = new Account();
        sourceAccount.setBalance(sourceAccountStartAmount);

        BigDecimal targetAccountStartAmount = BigDecimal.ZERO;
        BigDecimal targetAccountExpectedAmount = targetAccountStartAmount.add(transferAmount);
        Account targetAccount = new Account();
        targetAccount.setBalance(targetAccountStartAmount);

        Document document = new Document();
        document.setAmount(transferAmount);
        document.setSourceAccount(sourceAccount);
        document.setTargetAccount(targetAccount);
        document.setCreatedAt(LocalDateTime.now());

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionManager.createTransactions(document);

        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(accountRepository, times(2)).save(any(Account.class));
        assertEquals(sourceAccountExpectedAmount, sourceAccount.getBalance());
        assertEquals(targetAccountExpectedAmount, targetAccount.getBalance());
    }

    @Test
    public void testCreateTransactions_diffCurrency_success() {

        BigDecimal sourceAmount = BigDecimal.TEN;
        BigDecimal targetAmount = BigDecimal.ONE;

        BigDecimal sourceAccountStartAmount = BigDecimal.TEN;
        BigDecimal sourceAccountExpectedAmount = sourceAccountStartAmount.subtract(sourceAmount);
        Account sourceAccount = new Account();
        sourceAccount.setBalance(sourceAccountStartAmount);

        BigDecimal targetAccountStartAmount = BigDecimal.ZERO;
        BigDecimal targetAccountExpectedAmount = targetAccountStartAmount.add(targetAmount);
        Account targetAccount = new Account();
        targetAccount.setBalance(targetAccountStartAmount);

        Document document = new Document();
        document.setSourceAmount(sourceAmount);
        document.setTargetAmount(targetAmount);
        document.setSourceAccount(sourceAccount);
        document.setTargetAccount(targetAccount);
        document.setTargetCcy(targetAccount.getCcy());
        document.setSourceCcy(sourceAccount.getCcy());
        document.setTransferType(TransferType.FIAT_MULTI_CCY);
        document.setDocumentNumber("documentNumber");

        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transactionManager.createTransactions(document);

        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(accountRepository, times(2)).save(any(Account.class));
        assertEquals(sourceAccountExpectedAmount, sourceAccount.getBalance());
        assertEquals(targetAccountExpectedAmount, targetAccount.getBalance());
    }
}
