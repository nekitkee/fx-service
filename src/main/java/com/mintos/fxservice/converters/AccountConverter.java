package com.mintos.fxservice.converters;

import com.mintos.fxservice.dtos.account.AccountDto;
import com.mintos.fxservice.models.Account;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountConverter implements Converter<Account, AccountDto> {
    private final ModelMapper modelMapper;

    @Override
    public AccountDto convert(Account source) {
       return modelMapper.map(source, AccountDto.class);
    }
}
