package com.webee.service.impl;

import com.webee.dao.UsersMapper;
import com.webee.pojo.Users;
import com.webee.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UsersServiceImpl implements UsersService {


    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users findUserByUsernameAndPassword(String name, String password) {
        return usersMapper.findUserByUsernameAndPassword(name,password);
    }
}

