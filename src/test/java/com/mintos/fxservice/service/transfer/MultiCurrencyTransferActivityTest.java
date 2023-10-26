package com.mintos.fxservice.service.transfer;

import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.repositories.DocumentRepository;
import com.mintos.fxservice.services.ccy.CurrencyRateService;
import com.mintos.fxservice.services.transaction.TransactionManager;
import com.mintos.fxservice.services.transfer.activity.MultiCurrencyTransferActivity;
import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.services.transfer.TransferType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MultiCurrencyTransferActivityTest {
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private TransactionManager transactionManager;
    @Mock
    private CurrencyRateService currencyRateService;
    @InjectMocks
    private MultiCurrencyTransferActivity multiCurrencyTransferActivity;

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

        var sourceCcy = "EUR";
        var targetCcy = "USD";
        var amount = BigDecimal.TEN;
        Account sourceAccount = getAccount("sourceAccountNumber", sourceCcy, amount );
        Account targetAccount = getAccount("targetAccountNumber", targetCcy, BigDecimal.ZERO);
        TransferDetails transferDetails = getTransferDetails(sourceCcy, amount, sourceAccount, targetAccount);
        when(currencyRateService.getExchangeRate(sourceCcy,targetCcy)).thenReturn(BigDecimal.valueOf(1.12));

        Document document = multiCurrencyTransferActivity.processTransfer(transferDetails);

        verify(documentRepository).save(document);
        verify(transactionManager).createTransactions(document);

    }

    //Other cases same as in singleCurrencyTransfer.
}
