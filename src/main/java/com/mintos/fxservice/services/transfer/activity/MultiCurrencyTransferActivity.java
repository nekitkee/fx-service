package com.mintos.fxservice.services.transfer.activity;

import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.repositories.DocumentRepository;
import com.mintos.fxservice.services.ccy.CurrencyRateService;
import com.mintos.fxservice.services.transaction.TransactionManager;
import com.mintos.fxservice.services.transfer.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MultiCurrencyTransferActivity extends BaseTransferActivity {

    private final CurrencyRateService currencyRateService;

    @Autowired
    public MultiCurrencyTransferActivity(DocumentRepository documentRepository, TransactionManager transactionManager, CurrencyRateService currencyRateService) {
        super(documentRepository, transactionManager);
        this.currencyRateService = currencyRateService;
        super.transferType = TransferType.FIAT_MULTI_CCY;
    }

    @Override
    protected Document.DocumentBuilder performTransferInternal(TransferDetails transferDetails) {
        var sourceAccount = transferDetails.getSourceAccount();
        var targetAccount = transferDetails.getTargetAccount();
        var amount = transferDetails.getAmount();
        var ccy = transferDetails.getCcy();

        var rate = currencyRateService.getExchangeRate(sourceAccount.getCcy(), targetAccount.getCcy());
        var sourceAmount = calculateSourceAmount(amount, ccy, sourceAccount.getCcy(), rate);
        var targetAmount = calculateTargetAmount(amount, ccy, targetAccount.getCcy(), rate);

        checkAccountBalance(sourceAccount, sourceAmount);

        return createDocumentBuilder(transferDetails, rate, sourceAmount, targetAmount);
    }

    private Document.DocumentBuilder createDocumentBuilder(TransferDetails transferDetails, BigDecimal rate, BigDecimal sourceAmount, BigDecimal targetAmount) {
        var documentBuilder = createDocumentBuilder(transferDetails);
        documentBuilder.rate(rate);
        documentBuilder.sourceAmount(sourceAmount);
        documentBuilder.targetAmount(targetAmount);
        return documentBuilder;
    }

    private BigDecimal calculateSourceAmount(BigDecimal amount, String transferCcy, String sourceCcy, BigDecimal rate) {
        return transferCcy.equals(sourceCcy) ? amount.setScale(2, RoundingMode.HALF_UP) : amount.divide(rate, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTargetAmount(BigDecimal amount, String transferCcy, String targetCcy, BigDecimal rate) {
        return transferCcy.equals(targetCcy) ? amount.setScale(2, RoundingMode.HALF_UP) : amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
