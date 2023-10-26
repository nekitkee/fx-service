package com.mintos.fxservice.services.transfer.factory;

import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.services.transfer.activity.TransferActivity;

public interface ActivityFactory {
    TransferActivity getTransferActivity(TransferDetails transferDetails);
}
