package com.mintos.fxservice.services.transaction;

import com.mintos.fxservice.dtos.transction.TransactionDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionHistoryManager {
    List<TransactionDto> getTransactionsByAccount(String accountNumber, Pageable pageable);

}
