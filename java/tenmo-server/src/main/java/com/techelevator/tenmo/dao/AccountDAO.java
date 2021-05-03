package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceData;
import com.techelevator.tenmo.model.Transfers;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDAO {

    public BalanceData getBalanceGivenAnId(int id);

    public void sendMoney(int receiverId, int senderId, BigDecimal amountToAdd);

    public void requestMoney(int requesterId, int senderId, BigDecimal amountToAdd);

    public void transfer(int receiverId, int senderId, BigDecimal transferAmount);

    public List<Transfers> seeAllTransfers(int id);

}
