package com.techelevator.tenmo.models;

import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountTest {

@Test
    public void canInstantiateAccount() {
    Account account = new Account();
    account.setAccountId(5);
    account.setUserId(8);
    account.setBalance(new BigDecimal(8000));
    assertEquals(5, account.getAccountId());
    assertEquals(8, account.getUserId());
    assertEquals(new BigDecimal(8000), account.getBalance());
}

}