package com.mintos.fxservice.controllers;

import com.mintos.fxservice.dtos.transfer.DocumentDto;
import com.mintos.fxservice.dtos.transfer.TransferRequest;
import com.mintos.fxservice.services.transfer.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/transfers")
@AllArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<DocumentDto> transferFunds(@RequestBody TransferRequest transferRequest){
        var documentDto = transferService.transferFunds(transferRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(documentDto);
    }

}
