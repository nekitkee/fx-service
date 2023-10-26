package com.mintos.fxservice.services.client;

import com.mintos.fxservice.dtos.client.ClientCreateRequest;
import com.mintos.fxservice.dtos.client.ClientDto;

public interface ClientManager {
    ClientDto createClient(ClientCreateRequest clientCreateRequest);
}
