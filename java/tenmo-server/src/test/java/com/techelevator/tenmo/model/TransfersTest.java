package com.techelevator.tenmo.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class TransfersTest {

    @Test
    public void canInstantiateTransfersServer() {
        Transfers transfers = new Transfers();
        transfers.setTransferId(12);
        transfers.setTransferTypeId(6);
        transfers.setTransferStatusId(9);
        transfers.setAccountFrom(900);
        transfers.setAccountTo(7171);
        transfers.setAmount(new BigDecimal(19900));
        assertEquals(12, transfers.getTransferId());
        assertEquals(6, transfers.getTransferTypeId());
        assertEquals(9, transfers.getTransferStatusId());
        assertEquals(900, transfers.getAccountFrom());
        assertEquals(7171, transfers.getAccountTo());
        assertEquals(new BigDecimal(19900), transfers.getAmount());
    }
}