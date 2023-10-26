package com.mintos.fxservice.converters;

import com.mintos.fxservice.dtos.client.ClientDto;
import com.mintos.fxservice.models.Client;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class ClientConverter implements Converter<Client, ClientDto> {

    private final ModelMapper modelMapper;

    @Override
    public ClientDto convert(Client source) {
        return modelMapper.map(source, ClientDto.class);
    }
}
