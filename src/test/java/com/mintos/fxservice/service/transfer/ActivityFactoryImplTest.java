package com.mintos.fxservice.service.transfer;

import com.mintos.fxservice.exceptions.UnsupportedTransferTypeException;
import com.mintos.fxservice.services.transfer.factory.ActivityFactoryImpl;
import com.mintos.fxservice.services.transfer.activity.TransferActivity;
import com.mintos.fxservice.dtos.transfer.TransferDetails;
import com.mintos.fxservice.services.transfer.TransferType;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class ActivityFactoryImplTest {
    private ActivityFactoryImpl activityFactory;
    private List<TransferActivity> transferActivities;

    @BeforeEach
    public void setUp() {
        transferActivities = new ArrayList<>();
        activityFactory = new ActivityFactoryImpl(transferActivities);
    }

    @Test
    public void testGetTransferActivity_successful() {
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setTransferType(TransferType.FIAT_SINGLE_CCY);
        TransferActivity transferActivity = mock(TransferActivity.class);
        when(transferActivity.getTransferType()).thenReturn(TransferType.FIAT_SINGLE_CCY);
        transferActivities.add(transferActivity);

        TransferActivity result = activityFactory.getTransferActivity(transferDetails);

        assertEquals(transferActivity, result);
    }

    @Test
    public void testGetTransferActivity_unsupported_failure() {
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setTransferType(TransferType.FIAT_MULTI_CCY);
        TransferActivity transferActivity = mock(TransferActivity.class);
        when(transferActivity.getTransferType()).thenReturn(TransferType.FIAT_SINGLE_CCY);
        transferActivities.add(transferActivity);
        transferActivities.add(transferActivity);
        transferActivities.add(transferActivity);

        assertThrows(UnsupportedTransferTypeException.class, () -> activityFactory.getTransferActivity(transferDetails));

        verify(transferActivity, times(transferActivities.size())).getTransferType();

    }
}
