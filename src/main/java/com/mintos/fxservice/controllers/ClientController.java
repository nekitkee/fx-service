package com.mintos.fxservice.controllers;

import com.mintos.fxservice.dtos.client.ClientCreateRequest;
import com.mintos.fxservice.dtos.client.ClientDto;
import com.mintos.fxservice.services.client.ClientManager;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/clients")
@AllArgsConstructor
public class ClientController {

    private final ClientManager clientManager;
    @PostMapping
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientCreateRequest clientCreateRequest) {
        var clientDto = clientManager.createClient(clientCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientDto);
    }
}
