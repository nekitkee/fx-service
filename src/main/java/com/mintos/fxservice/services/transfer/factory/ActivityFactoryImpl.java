package com.mintos.fxservice.services.transfer.factory;

import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.exceptions.UnsupportedTransferTypeException;
import com.mintos.fxservice.services.transfer.activity.TransferActivity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ActivityFactoryImpl implements ActivityFactory {

    private final List<TransferActivity> transferActivities;
    public TransferActivity getTransferActivity(TransferDetails transferDetails) {
        for (TransferActivity transferActivity : transferActivities){
            if(transferActivity.getTransferType().equals(transferDetails.getTransferType()) ){
                return transferActivity;
            }
        }

        throw new UnsupportedTransferTypeException();
    }
}
