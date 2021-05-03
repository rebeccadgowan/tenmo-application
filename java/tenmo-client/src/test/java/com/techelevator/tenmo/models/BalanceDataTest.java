package com.techelevator.tenmo.models;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BalanceDataTest {

    @Test
    public void canInstantiateBalanceData() {
        BalanceData balanceData = new BalanceData();
        balanceData.setBalance(new BigDecimal(777));
        assertEquals(new BigDecimal(777), balanceData.getBalance());
    }

}