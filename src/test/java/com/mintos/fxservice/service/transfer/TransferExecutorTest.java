package com.mintos.fxservice.service.transfer;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.mintos.fxservice.dtos.transfer.DocumentDto;
import com.mintos.fxservice.dtos.transfer.TransferRequest;
import com.mintos.fxservice.exceptions.AccountNotFoundException;
import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.repositories.AccountRepository;
import com.mintos.fxservice.services.transfer.TransferExecutor;
import com.mintos.fxservice.services.transfer.activity.TransferActivity;
import com.mintos.fxservice.services.transfer.factory.ActivityFactory;
import com.mintos.fxservice.dtos.transfer.TransferDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransferExecutorTest {
    @InjectMocks
    private TransferExecutor transferExecutor;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ActivityFactory activityFactory;

    @Mock
    private Converter<Document, DocumentDto> dtoConverter;

    @Test
    public void testTransferFunds_sameCurrency_successful() {
        var ccy = "EUR";
        TransferRequest request = new TransferRequest();
        request.setSourceAccountNumber("sourceAccountNumber");
        request.setTargetAccountNumber("targetAccountNumber");
        request.setAmount(BigDecimal.TEN);
        request.setCcy(ccy);
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("sourceAccountNumber");
        sourceAccount.setCcy(ccy);
        when(accountRepository.findByAccountNumber("sourceAccountNumber")).thenReturn(Optional.of(sourceAccount));
        Account targetAccount = new Account();
        targetAccount.setAccountNumber("targetAccountNumber");
        targetAccount.setCcy(ccy);
        when(accountRepository.findByAccountNumber("targetAccountNumber")).thenReturn(Optional.of(targetAccount));
        TransferActivity transferActivityMock = mock(TransferActivity.class);
        when(activityFactory.getTransferActivity(any(TransferDetails.class))).thenReturn(transferActivityMock);
        Document document = new Document();
        when(transferActivityMock.processTransfer(any(TransferDetails.class))).thenReturn(document);
        DocumentDto documentDto = new DocumentDto();
        when(dtoConverter.convert(document)).thenReturn(documentDto);

        DocumentDto result = transferExecutor.transferFunds(request);

        verify(accountRepository, times(2)).findByAccountNumber(anyString());
        verify(activityFactory).getTransferActivity(any(TransferDetails.class));
        verify(transferActivityMock).processTransfer(any(TransferDetails.class));
        verify(dtoConverter).convert(document);
        assertEquals(documentDto, result);
    }

    @Test
    public void testTransferFunds_diffCurrency_successful() {
        var sourceCcy = "EUR";
        var targetCcy = "USD";
        TransferRequest request = new TransferRequest();
        request.setSourceAccountNumber("sourceAccountNumber");
        request.setTargetAccountNumber("targetAccountNumber");
        request.setAmount(BigDecimal.TEN);
        request.setCcy(sourceCcy);
        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("sourceAccountNumber");
        sourceAccount.setCcy(sourceCcy);
        when(accountRepository.findByAccountNumber("sourceAccountNumber")).thenReturn(Optional.of(sourceAccount));
        Account targetAccount = new Account();
        targetAccount.setAccountNumber("targetAccountNumber");
        targetAccount.setCcy(targetCcy);
        when(accountRepository.findByAccountNumber("targetAccountNumber")).thenReturn(Optional.of(targetAccount));
        TransferActivity transferActivityMock = mock(TransferActivity.class);
        when(activityFactory.getTransferActivity(any(TransferDetails.class))).thenReturn(transferActivityMock);
        Document document = new Document();
        when(transferActivityMock.processTransfer(any(TransferDetails.class))).thenReturn(document);
        DocumentDto documentDto = new DocumentDto();
        when(dtoConverter.convert(document)).thenReturn(documentDto);

        DocumentDto result = transferExecutor.transferFunds(request);

        verify(accountRepository, times(2)).findByAccountNumber(anyString());
        verify(activityFactory).getTransferActivity(any(TransferDetails.class));
        verify(transferActivityMock).processTransfer(any(TransferDetails.class));
        verify(dtoConverter).convert(document);
        assertEquals(documentDto, result);
    }

    @Test
    public void testTransferFunds_notExistingAccount_failure() {
        TransferRequest request = new TransferRequest();
        request.setSourceAccountNumber("sourceAccountNumber");
        request.setTargetAccountNumber("notExistingAccountNumber");

        Account sourceAccount = new Account();
        sourceAccount.setAccountNumber("sourceAccountNumber");
        when(accountRepository.findByAccountNumber("sourceAccountNumber")).thenReturn(Optional.of(sourceAccount));
        Account targetAccount = new Account();
        targetAccount.setAccountNumber("targetAccountNumber");
        when(accountRepository.findByAccountNumber("notExistingAccountNumber")).thenThrow(AccountNotFoundException.class);

        assertThrows(AccountNotFoundException.class, () -> transferExecutor.transferFunds(request));

        verify(accountRepository, times(2)).findByAccountNumber(anyString());
        verifyNoInteractions(activityFactory);
        verifyNoInteractions(dtoConverter);
    }
}
