package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountController {

    private AccountDao accountDao;



    public AccountController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/balance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int id){
        System.out.println(id);
        return accountDao.getBalance(id);
    }

    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public Account getAccountById(@PathVariable int id) {
        System.out.println(id);
        return accountDao.getAccount(id);
    }

    @RequestMapping(path = "account/{id}", method = RequestMethod.PUT)
    public Account updateAccountById(@PathVariable int id, @RequestBody Account account) {
        return accountDao.updateAccount(account);
    }

}
