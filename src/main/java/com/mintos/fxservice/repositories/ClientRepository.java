package com.mintos.fxservice.repositories;

import com.mintos.fxservice.models.Account;
import com.mintos.fxservice.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientNumber(String clientNumber);
}
