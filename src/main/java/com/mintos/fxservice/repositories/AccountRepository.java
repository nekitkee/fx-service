package com.mintos.fxservice.repositories;

import com.mintos.fxservice.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.client.clientNumber = :clientNumber")
    List<Account> findByClientNumber(String clientNumber);
    Optional<Account> findByAccountNumber(String accountNumber);







}
