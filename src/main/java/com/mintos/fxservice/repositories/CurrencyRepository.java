package com.mintos.fxservice.repositories;
import com.mintos.fxservice.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository  extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(String code);
}
