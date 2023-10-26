package com.mintos.fxservice.service.client;

import com.mintos.fxservice.dtos.client.ClientCreateRequest;
import com.mintos.fxservice.dtos.client.ClientDto;
import com.mintos.fxservice.models.Client;
import com.mintos.fxservice.repositories.ClientRepository;
import com.mintos.fxservice.services.client.ClientManagerImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientManagerImplTest {

    @InjectMocks
    private ClientManagerImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private Converter<Client, ClientDto> converter;

    @Test
    public void testCreateClient_successfully() {
        ClientCreateRequest request = new ClientCreateRequest();
        request.setName("Johny Joe");

        Client createdClient = new Client();
        createdClient.setName(request.getName());
        createdClient.setClientNumber(UUID.randomUUID().toString());

        when(clientRepository.save(any(Client.class))).thenReturn(createdClient);
        when(converter.convert(createdClient)).thenReturn(new ClientDto());

        ClientDto clientDto = clientService.createClient(request);

        verify(clientRepository, times(1)).save(any(Client.class));
        verify(converter, times(1)).convert(createdClient);
        assertNotNull(clientDto);
    }
}

