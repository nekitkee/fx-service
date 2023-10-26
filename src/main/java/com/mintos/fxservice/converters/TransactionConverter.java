package com.mintos.fxservice.converters;

import com.mintos.fxservice.dtos.transction.TransactionDto;
import com.mintos.fxservice.models.Transaction;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionConverter implements Converter<Transaction, TransactionDto> {

    private final ModelMapper modelMapper;

    @Override
    public TransactionDto convert(Transaction source) {
        return modelMapper.map(source, TransactionDto.class);
    }
}