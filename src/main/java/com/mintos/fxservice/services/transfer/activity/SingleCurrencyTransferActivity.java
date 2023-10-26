package com.mintos.fxservice.services.transfer.activity;
import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.repositories.DocumentRepository;
import com.mintos.fxservice.services.transaction.TransactionManager;
import com.mintos.fxservice.services.transfer.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SingleCurrencyTransferActivity extends BaseTransferActivity {

    @Autowired
    public SingleCurrencyTransferActivity(DocumentRepository documentRepository, TransactionManager transactionManager) {
        super(documentRepository, transactionManager);
        super.transferType = TransferType.FIAT_SINGLE_CCY;
    }

    @Override
    protected Document.DocumentBuilder performTransferInternal(TransferDetails transferDetails) {
        var sourceAccount = transferDetails.getSourceAccount();
        var amount = transferDetails.getAmount();
        checkAccountBalance(sourceAccount, amount);
        return createDocumentBuilder(transferDetails);
    }
}
