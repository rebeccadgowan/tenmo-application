package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.BalanceData;
import com.techelevator.tenmo.model.Transfers;
import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcAccountDAOTest {

    private static SingleConnectionDataSource dataSource;
    private AccountDAO accountDAO;
    private UserDAO userDAO;

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

        userDAO = new JdbcUserDAO(jdbcTemplateUser);
        accountDAO = new JdbcAccountDAO(dataSource);
    }

    @After
    public void doAfterEveryTest() throws Exception{
        dataSource.getConnection().rollback();
    }

    @Test
    public void get_balance_given_user_id() {
        BalanceData balanceData = accountDAO.getBalanceGivenAnId(1009);
        assertEquals(new BigDecimal(1000).setScale(2, RoundingMode.HALF_UP), balanceData.getBalance());
        System.out.println(balanceData);
    }

    @Test
    public void can_send_money() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        accountDAO.sendMoney(1009, 1010, new BigDecimal(50));
        String sql = "SELECT balance FROM accounts WHERE user_id = ?";
        SqlRowSet sqlRowSetReceiver = jdbcTemplate.queryForRowSet(sql, 1009);
        BigDecimal receiverBalance = new BigDecimal(0);
        while(sqlRowSetReceiver.next()) {
            receiverBalance = sqlRowSetReceiver.getBigDecimal("balance");
        }
        SqlRowSet sqlRowSetSender = jdbcTemplate.queryForRowSet(sql, 1010);
        BigDecimal senderBalance = new BigDecimal(0);
        while(sqlRowSetSender.next()) {
            senderBalance = sqlRowSetSender.getBigDecimal("balance");
        }

        Assert.assertEquals(new BigDecimal(1050).setScale(2, RoundingMode.HALF_UP), receiverBalance);
        Assert.assertEquals(new BigDecimal(950).setScale(2, RoundingMode.HALF_UP), senderBalance);
    }

    @Test
    public void see_all_transfers() {
        accountDAO.transfer(1009, 1010, new BigDecimal(100));
        List<Transfers> allTransfers = accountDAO.seeAllTransfers(1009);
        Assert.assertEquals(allTransfers.get(0).getAccountTo(), 2009);
    }
}