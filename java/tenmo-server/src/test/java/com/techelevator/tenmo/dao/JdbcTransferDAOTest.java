package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDetails;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcTransferDAOTest {

    private static SingleConnectionDataSource dataSource;
    private TransfersDAO transfersDAO;
    private UserDAO userDAO;
    private AccountDAO accountDAO;

    @BeforeClass
    public static void setup() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");
        dataSource.setAutoCommit(false);
    }

    @AfterClass
    public static void cleanup() {
        dataSource.destroy();
    }

    @Before
    public void setupBeforeTest() {
        // first user
        String sqlUser = "INSERT INTO users (user_id, username, password_hash) VALUES (?, 'testname9', 'testpasswordhash')";
        JdbcTemplate jdbcTemplateUser = new JdbcTemplate(dataSource);
        jdbcTemplateUser.update(sqlUser, 1009);

        String sql = "INSERT INTO accounts (account_id, user_id, balance) VALUES (?, 1009, 1000)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, 2009);

        // second user
        String sqlUser2 = "INSERT INTO users (user_id, username, password_hash) VALUES (?, 'testname5', 'testpasswordhash')";
        JdbcTemplate jdbcTemplateUser2 = new JdbcTemplate(dataSource);
        jdbcTemplateUser2.update(sqlUser2, 1010);

        String sqlAccount2 = "INSERT INTO accounts (account_id, user_id, balance) VALUES (?, 1010, 1000)";
        JdbcTemplate jdbcTemplateAccount2 = new JdbcTemplate(dataSource);
        jdbcTemplateAccount2.update(sqlAccount2, 2010);

        // fake transfer
        String sqlTransfer = "INSERT INTO transfers(transfer_id, transfer_type_id, transfer_status_id, account_from, " +
                "account_to, amount) VALUES(?, 2, 2, 2009, 2010, 200)";
        JdbcTemplate jdbcTemplateTransfer = new JdbcTemplate(dataSource);
        jdbcTemplateTransfer.update(sqlTransfer, 3200);

        userDAO = new JdbcUserDAO(jdbcTemplateUser);
        accountDAO = new JdbcAccountDAO(dataSource);
        transfersDAO = new JdbcTransferDAO(dataSource);
    }

    @After
    public void doAfterEveryTest() throws Exception{
        dataSource.getConnection().rollback();
    }

    @Test
    public void see_transfer_details() {
        List<TransferDetails> transferDetails = transfersDAO.seeTransferDetails(3010);
        Assert.assertEquals(transferDetails.get(0).getTransferId(), 3010);
    }

}