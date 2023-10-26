package com.mintos.fxservice.services.transaction;

import com.mintos.fxservice.dtos.transction.TransactionDto;
import com.mintos.fxservice.models.Transaction;
import com.mintos.fxservice.repositories.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionHistoryManagerImpl implements TransactionHistoryManager {

    private final TransactionRepository transactionRepository;
    private final Converter<Transaction, TransactionDto> dtoConverter;
    @Override
    public List<TransactionDto> getTransactionsByAccount(String accountNumber, Pageable pageable) {
        return transactionRepository
                .findByAccountAccountNumber(accountNumber, pageable)
                .stream()
                .map(dtoConverter::convert)
                .toList();
    }
}
