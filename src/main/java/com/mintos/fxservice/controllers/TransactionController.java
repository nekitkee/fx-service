package com.mintos.fxservice.controllers;

import com.mintos.fxservice.dtos.transction.TransactionDto;
import com.mintos.fxservice.services.transaction.TransactionHistoryManager;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Nullable;
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

    @GetMapping("/accounts/{accountNumber}/transactions")
    public List<TransactionDto> getTransactionsByAccountNumber(@PathVariable String accountNumber,
                                                               @PageableDefault(sort = "createdAt" , direction = Sort.Direction.DESC)
                                                               @Parameter(description = "use \"offset\" and \"limit\" parameters for paging")
                                                               @Nullable Pageable pageable) {
        return transactionHistoryManager.getTransactionsByAccount(accountNumber, pageable);
    }
}
