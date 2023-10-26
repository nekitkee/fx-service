package com.mintos.fxservice.services.transaction;

import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Transaction;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.repositories.AccountRepository;
import com.mintos.fxservice.repositories.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class TransactionManagerImpl implements TransactionManager{
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void createTransactions(Document transfer) {
        createDebitTransaction(transfer);
        createCreditTransaction(transfer);
    }

    private void createDebitTransaction(Document document) {

        var amount = document.getSourceAmount() == null ? document.getAmount() : document.getSourceAmount();
        Transaction debitTransaction = Transaction.builder()
                .transactionNumber(UUID.randomUUID().toString())
                .document(document)
                .amount(amount.negate())
                .account(document.getSourceAccount())
                .createdAt(document.getCreatedAt())
                .build();

        deductAccountBalance(document.getSourceAccount(), amount);
        transactionRepository.save(debitTransaction);
    }

    private void createCreditTransaction(Document document) {

        var amount = document.getTargetAmount() == null ? document.getAmount() : document.getTargetAmount();
        Transaction creditTransaction = Transaction.builder()
                .transactionNumber(UUID.randomUUID().toString())
                .document(document)
                .amount(amount)
                .account(document.getTargetAccount())
                .createdAt(document.getCreatedAt())
                .build();

        increaseAccountBalance(document.getTargetAccount(), amount);
        transactionRepository.save(creditTransaction);

    }

    private void deductAccountBalance(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    private void increaseAccountBalance(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

}
