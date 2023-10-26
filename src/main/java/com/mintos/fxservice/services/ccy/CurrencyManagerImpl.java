package com.mintos.fxservice.services.ccy;

import com.mintos.fxservice.repositories.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrencyManagerImpl implements CurrencyManager {

    CurrencyRepository currencyRepository;
    public boolean isCcyEnabled(String ccy){
       return currencyRepository.findByCode(ccy).isPresent();
    }
}
