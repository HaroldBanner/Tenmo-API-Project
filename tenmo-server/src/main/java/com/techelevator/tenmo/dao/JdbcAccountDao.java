package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private BigDecimal balance;

    private Account account;
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        // this.balance = balance;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccount(int id) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        while (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public BigDecimal getBalance(int id) {
        String sql = "SELECT balance FROM account WHERE user_id = ?;";
        BigDecimal results = jdbcTemplate.queryForObject(sql, BigDecimal.class, id);
        return results;
    }

    // We won't need this if we handle it in the transfer method
    @Override
    public BigDecimal addFunds(int id, BigDecimal withdrawalAmount) {
        account.getBalance().add(withdrawalAmount);
        return null;
    }

    // We won't need this if we handle it in the transfer method
    @Override
    public BigDecimal withdrawFunds(int id, BigDecimal withdrawalAmount) {
        account.getBalance().subtract(withdrawalAmount);
        return null;
    }

    @Override
    public Account updateAccount(Account account) {
        String sql = "UPDATE account SET balance = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, account.getBalance(), account.getUserId());
        return account;
    }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }


}
