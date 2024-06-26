package com.webee.dao;

import com.webee.pojo.Users;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UsersMapper {
    //登录
    @Select("SELECT * FROM users WHERE name = #{name} AND password = #{password}")
    Users findUserByUsernameAndPassword(@Param("name") String name, @Param("password") String password);


    //注册
    @Insert("INSERT INTO users(name, password) " +
            "VALUES(#{name}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    boolean saveUser(Users user);
}
