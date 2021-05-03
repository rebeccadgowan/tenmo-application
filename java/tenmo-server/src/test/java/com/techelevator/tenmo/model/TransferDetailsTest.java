package com.techelevator.tenmo.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class TransferDetailsTest {

    @Test
    public void canInstantiateTransferDetailsServer() {
        TransferDetails transferDetails = new TransferDetails();
        transferDetails.setTransferId(6363);
        transferDetails.setTransferTypeDescription("Balloon");
        transferDetails.setTransferStatusDescription("Bad");
        transferDetails.setAmount(new BigDecimal(3434));
        transferDetails.setUsername("Jack");
        assertEquals(6363, transferDetails.getTransferId());
        assertEquals("Balloon", transferDetails.getTransferTypeDescription());
        assertEquals("Bad", transferDetails.getTransferStatusDescription());
        assertEquals(new BigDecimal(3434), transferDetails.getAmount());
        assertEquals("Jack", transferDetails.getUsername());
    }
}