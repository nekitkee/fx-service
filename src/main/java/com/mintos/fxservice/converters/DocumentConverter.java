package com.mintos.fxservice.converters;

import com.mintos.fxservice.dtos.transfer.DocumentDto;
import com.mintos.fxservice.models.Document;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DocumentConverter implements Converter<Document, DocumentDto> {

    private final ModelMapper modelMapper;

    @Override
    public DocumentDto convert(Document source) {
        return modelMapper.map(source, DocumentDto.class);
    }
}
