package com.techelevator.tenmo.models;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransferDetailsTest {

    @Test
    public void canInstantiateTransferDetails() {
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setTransferId(7);
        transferDetails.setTransferTypeDescription("Testing");
        transferDetails.setTransferStatusDescription("Good");
        transferDetails.setAmount(new BigDecimal(2000));
        transferDetails.setUsername("Bob");
        assertEquals(7, transferDetails.getTransferId());
        assertEquals("Testing", transferDetails.getTransferTypeDescription());
        assertEquals("Good", transferDetails.getTransferStatusDescription());
        assertEquals(new BigDecimal(2000), transferDetails.getAmount());
        assertEquals("Bob", transferDetails.getUsername());
    }

}