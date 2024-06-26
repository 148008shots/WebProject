package com.webee.service;

import com.webee.pojo.Users;

public interface UsersService {

    Users findUserByUsernameAndPassword(String name, String password);
    //插入注册
    boolean saveUser(Users user);
}
