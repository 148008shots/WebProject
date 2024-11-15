package com.hh.service;

import com.hh.pojo.User;

import java.util.List;

public interface UserService {
    User findByUserName(String username);

    void register(String username, String password,String phone);

    void update(User user);

    void updateAvatar(String avatarUrl);

    void updatePwd(String new_pwd);

    List<User> fetchAllUsers();

    void updateUser(User user);

    void deleteUser(Integer id);

    User findByUserNameAndPhone(String username, String phone);

    void reUpdatePwd(String newPassword, String username);

    void resetUpdatePassword(String username, String newPassword);
}




