package com.mintos.fxservice.services.client;

import com.mintos.fxservice.dtos.client.ClientCreateRequest;
import com.mintos.fxservice.dtos.client.ClientDto;
import com.mintos.fxservice.models.Client;
import com.mintos.fxservice.repositories.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientManagerImpl implements ClientManager {

    private final ClientRepository clientRepository;
    private final Converter<Client, ClientDto> converter;
    @Override
    public ClientDto createClient(ClientCreateRequest clientCreateRequest) {
        var client = createClientEntity(clientCreateRequest);
        return converter.convert(client);
    }

    private Client createClientEntity(ClientCreateRequest clientCreateRequest) {
        var client = new Client();
        client.setName(clientCreateRequest.getName());
        client.setClientNumber(UUID.randomUUID().toString());
        return clientRepository.save(client);
    }
}
