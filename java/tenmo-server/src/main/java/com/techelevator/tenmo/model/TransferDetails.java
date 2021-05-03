package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDetails {
    private int transferId;
    private String transferTypeDescription;
    private String transferStatusDescription;
    private BigDecimal amount;
    private String username;

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int sqlRowSetTransferId) {
        this.transferId = sqlRowSetTransferId;
    }

    public String getTransferTypeDescription() {
        return transferTypeDescription;
    }

    public void setTransferTypeDescription(String transferTypeDescription) {
        this.transferTypeDescription = transferTypeDescription;
    }

    public String getTransferStatusDescription() {
        return transferStatusDescription;
    }

    public void setTransferStatusDescription(String transferStatusDescription) {
        this.transferStatusDescription = transferStatusDescription;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
