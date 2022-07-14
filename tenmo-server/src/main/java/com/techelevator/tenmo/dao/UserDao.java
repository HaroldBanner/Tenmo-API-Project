package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    String findUserById(int id);

    List<User> findAll();

    List<User> listAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    Double findBalanceByUserId(int id);

    String findUsernameByAccountId(int accountId);
}
