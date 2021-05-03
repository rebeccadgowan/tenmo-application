package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDetails;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransfersDAO{

    private JdbcTemplate template;

    public JdbcTransferDAO(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public List<TransferDetails> seeTransferDetails(int transferId) {
        String sql = "SELECT transfers.transfer_id, transfer_types.transfer_type_desc, transfer_statuses.transfer_status_desc, transfers.amount, users.username " +
                "FROM transfers " +
                "INNER JOIN transfer_types ON transfers.transfer_type_id = transfer_types.transfer_type_id " +
                "INNER JOIN  transfer_statuses ON transfers.transfer_status_id = transfer_statuses.transfer_status_id  " +
                "INNER JOIN accounts ON transfers.account_from = accounts.account_id " +
                "INNER JOIN users ON accounts.user_id = users.user_id " +
                "WHERE transfers.transfer_id = ?";

        List<TransferDetails> transferDetailsList = new ArrayList<TransferDetails>();
        SqlRowSet sqlRowSet = template.queryForRowSet(sql, transferId);

        while(sqlRowSet.next()) {
            TransferDetails transferDetails = new TransferDetails();

            transferDetails.setTransferId(sqlRowSet.getInt("transfer_id"));
            transferDetails.setTransferTypeDescription(sqlRowSet.getString("transfer_type_desc"));
            transferDetails.setTransferStatusDescription(sqlRowSet.getString("transfer_status_desc"));
            transferDetails.setAmount(sqlRowSet.getBigDecimal("amount"));
            transferDetails.setUsername(sqlRowSet.getString("username"));

            transferDetailsList.add(transferDetails);
        }

        return transferDetailsList;

    }
}
