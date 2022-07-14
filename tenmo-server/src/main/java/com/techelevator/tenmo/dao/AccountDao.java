package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {


    Account getAccount(int id);

    BigDecimal getBalance(int id);
    BigDecimal addFunds(int id, BigDecimal withdrawalAmount);
    BigDecimal withdrawFunds(int id, BigDecimal withdrawalAmount);

    Account updateAccount(Account account);
}
