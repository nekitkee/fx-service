package com.mintos.fxservice.services.transfer.activity;

import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.models.Document;
import com.mintos.fxservice.services.transfer.TransferType;

public interface TransferActivity {
    Document processTransfer(TransferDetails transferDetails);
    TransferType getTransferType();
}
