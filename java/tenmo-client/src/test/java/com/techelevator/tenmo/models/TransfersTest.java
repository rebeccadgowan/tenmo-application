package com.techelevator.tenmo.models;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class TransfersTest {

    @Test
    public void canInstantiateTransfers() {
        Transfers transfers = new Transfers();
        transfers.setTransferId(4);
        transfers.setTransferTypeId(90);
        transfers.setTransferStatusId(3);
        transfers.setAccountFrom(6);
        transfers.setAccountTo(1);
        transfers.setAmount(new BigDecimal(888));
        assertEquals(4, transfers.getTransferId());
        assertEquals(90, transfers.getTransferTypeId());
        assertEquals(3, transfers.getTransferStatusId());
        assertEquals(6, transfers.getAccountFrom());
        assertEquals(1, transfers.getAccountTo());
        assertEquals(new BigDecimal(888), transfers.getAmount());
    }

}