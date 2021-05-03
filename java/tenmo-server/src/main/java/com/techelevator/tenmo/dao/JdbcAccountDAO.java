package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceData;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDAO implements AccountDAO {

    private JdbcTemplate template;

    public JdbcAccountDAO(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public BalanceData getBalanceGivenAnId(int id) {

        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet rowSet = template.queryForRowSet(sql, id);

        BalanceData balanceData = new BalanceData();

        if(rowSet.next()) {
            String balance = rowSet.getString("balance");
            BigDecimal balanceBD = new BigDecimal(balance);
            balanceData.setBalance(balanceBD);
        }
        return balanceData;
    }

    @Override
    public void sendMoney(int receiverId, int senderId, BigDecimal amountToAdd) {
        BalanceData senderBalanceData = new BalanceData();
        BalanceData receiverBalanceData = new BalanceData();
        String selectSql = "SELECT balance FROM accounts WHERE user_id = ?";

        SqlRowSet senderRowSet = template.queryForRowSet(selectSql, senderId);

        if(senderRowSet.next()) {
            String senderBalance = senderRowSet.getString("balance");
            BigDecimal balanceBD = new BigDecimal(senderBalance);
            senderBalanceData.setBalance(balanceBD);
        }

        SqlRowSet receiverRowSet = template.queryForRowSet(selectSql, receiverId);
        if(receiverRowSet.next()) {
            String receiverBalance = receiverRowSet.getString("balance");
            BigDecimal balanceBD = new BigDecimal(receiverBalance);
            receiverBalanceData.setBalance(balanceBD);
        }
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        BigDecimal amountSubtract = senderBalanceData.getBalance().subtract(amountToAdd);

        template.update(sql, amountSubtract, senderId);
        BigDecimal amountAdd = amountToAdd.add(receiverBalanceData.getBalance());
        template.update(sql, amountAdd, receiverId);
    }

    @Override
    public void requestMoney(int requesterId, int senderId, BigDecimal amountToAdd) {
        BalanceData requesterBalanceData = new BalanceData();
        BalanceData senderBalanceData = new BalanceData();
        String selectSql = "SELECT balance FROM accounts WHERE user_id = ?";

        SqlRowSet requesterRowSet = template.queryForRowSet(selectSql, requesterId);

        if(requesterRowSet.next()) {
            String requesterBalance = requesterRowSet.getString("balance");
            BigDecimal balanceBD = new BigDecimal(requesterBalance);
            requesterBalanceData.setBalance(balanceBD);
        }

        SqlRowSet senderRowSet = template.queryForRowSet(selectSql, senderId);
        if(senderRowSet.next()) {
            String senderBalance = senderRowSet.getString("balance");
            BigDecimal balanceBD = new BigDecimal(senderBalance);
            senderBalanceData.setBalance(balanceBD);
        }

        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_to, account_from, amount) " +
                "VALUES (1, 1, (SELECT account_id FROM accounts WHERE user_id = ?), (SELECT account_id FROM accounts WHERE user_id = ?), ?)";

        template.update(sql, requesterId, senderId, amountToAdd);

    }

    @Override
    public void transfer(int receiverId, int senderId, BigDecimal transferAmount) {
        String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_to, account_from, amount) " +
                "VALUES (2, 2, (SELECT account_id FROM accounts WHERE user_id = ?), (SELECT account_id FROM accounts WHERE user_id = ?), ?)";
//
        template.update(sql, receiverId, senderId, transferAmount);

    }

    @Override
    public List<Transfers> seeAllTransfers(int id) {
        String sql = "SELECT * FROM transfers WHERE account_to = (SELECT account_id FROM accounts WHERE user_id = ?) " +
                "OR account_from = (SELECT account_id FROM accounts WHERE user_id = ?)";
        int accountFromId = id;

        List<Transfers> transfersList = new ArrayList<Transfers>();

        SqlRowSet sqlRowSet = template.queryForRowSet(sql, id, accountFromId);

        while(sqlRowSet.next()) {
            Transfers transfer = new Transfers();
            transfer.setTransferId(sqlRowSet.getInt("transfer_id"));
            transfer.setTransferTypeId(sqlRowSet.getInt("transfer_type_id"));
            transfer.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
            transfer.setAccountFrom(sqlRowSet.getInt("account_from"));
            transfer.setAccountTo(sqlRowSet.getInt("account_to"));
            transfer.setAmount(sqlRowSet.getBigDecimal("amount"));
            transfersList.add(transfer);
        }
            return transfersList;
    }

}
