package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import javax.lang.model.element.NestingKind;
import java.util.List;

@RestController
public class UserController {

    private UserDao userdao;

    public UserController(UserDao userdao) {
        this.userdao = userdao;
    }

    //methods for authorization of Users in user table
    @RequestMapping(path = "/users/", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userdao.findAll();
    }

    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    public String getUserById(@PathVariable int id) {
        return userdao.findUserById(id);
    }

    @RequestMapping(path = "/users/{username}", method = RequestMethod.GET)
    public int getUserIdFromUserName(@PathVariable String username){
       return userdao.findIdByUsername(username);
    }

    @RequestMapping(path = "/username/{accountId}", method = RequestMethod.GET)
    public String getUserByAccountId(@PathVariable int accountId){
        return userdao.findUsernameByAccountId(accountId);
    }

}
