package com.techelevator.tenmo.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class BalanceDataTest {

    @Test
    public void canInstantiateBalanceDataServer() {
        BalanceData balanceData = new BalanceData();
        balanceData.setBalance(new BigDecimal(565656));
        assertEquals(new BigDecimal(565656), balanceData.getBalance());
    }
}