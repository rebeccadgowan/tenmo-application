package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDetails;

import java.util.List;

public interface TransfersDAO {
    public List<TransferDetails> seeTransferDetails(int transferId);
}
