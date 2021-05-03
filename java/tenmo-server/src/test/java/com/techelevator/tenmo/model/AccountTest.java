package com.techelevator.tenmo.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

@Test
public void canInstantiateAccountServer() {
    Account account = new Account();
    account.setAccountId(44);
    account.setUserId(10);
    account.setBalance(new BigDecimal(99999));
    assertEquals(44, account.getAccountId());
    assertEquals(10, account.getUserId());
    assertEquals(new BigDecimal(99999), account.getBalance());
}
}