package com.mintos.fxservice.services.transaction;

import com.mintos.fxservice.models.Document;

public interface TransactionManager {
    void createTransactions(Document transfer);
}
