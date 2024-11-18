package com.hh.service.impl;

import com.hh.service.UserService;
import com.hh.mapper.UserMapper;
import com.hh.pojo.User;
import com.hh.utils.Md5Util;
import com.hh.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        User u = userMapper.findByUserName(username);
        return u;
    }

    @Override
    public void register(String username, String password,String phone) {
        //密码加密
        String md5String = Md5Util.getMD5String(password);
        //添加
        userMapper.add(username,md5String,phone);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {

        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl,id);
    }


    @Override
    public List<User> fetchAllUsers() {
        return userMapper.findAllUsers();
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateInfo(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteUserByUsername(id);
    }

    @Override
    public User findByUserNameAndPhone(String username, String phone) {
        return userMapper.selectByUserNameAndPhone(username, phone);
    }

    @Override
    public void reUpdatePwd(String newPassword, String username) {

    }

    @Override
    public void updatePwd(String new_pwd) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updatePwd(Md5Util.getMD5String(new_pwd), id);
    }

    @Override
    public void resetUpdatePassword(String username, String newPassword) {
        userMapper.resetUpdatePwd(Md5Util.getMD5String(newPassword), username);
    }

    @Override
    public User findByUserPhone(String phone) {
        User u = userMapper.findByUserPhone(phone);
        return u;
    }
}
