package com.mintos.fxservice.dtos.transction;

import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Document;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private String transactionNumber;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private String documentNumber;
    private String accountNumber;
}
