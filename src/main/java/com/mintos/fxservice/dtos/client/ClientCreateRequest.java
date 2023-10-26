package com.mintos.fxservice.dtos.client;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ClientCreateRequest {

    @NotEmpty(message = "Client name should be provided")
    private String name;
}
