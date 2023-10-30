package com.mintos.fxservice.controllers;

import com.mintos.fxservice.dtos.transction.TransactionDto;
import com.mintos.fxservice.services.transaction.TransactionHistoryManager;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class TransactionController {

    private final TransactionHistoryManager transactionHistoryManager;

    //?sort=createdAt,desc&size=5&page=3
    @GetMapping("/account/{accountNumber}/transactions")
    public List<TransactionDto> getTransactionsByAccountNumber(@PathVariable String accountNumber,
                                                               @PageableDefault(sort = "createdAt" , direction = Sort.Direction.DESC)
                                                                 Pageable pageable) {
        return transactionHistoryManager.getTransactionsByAccount(accountNumber, pageable);
    }
}
