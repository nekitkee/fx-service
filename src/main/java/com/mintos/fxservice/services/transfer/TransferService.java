package com.mintos.fxservice.services.transfer;

import com.mintos.fxservice.dtos.transfer.DocumentDto;
import com.mintos.fxservice.dtos.transfer.TransferRequest;

public interface TransferService {
    DocumentDto transferFunds(TransferRequest transferRequest);
}
