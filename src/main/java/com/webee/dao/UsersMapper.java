package com.webee.dao;

import com.webee.pojo.Users;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UsersMapper {

    @Select("SELECT * FROM users WHERE name = #{name} AND password = #{password}")
    Users findUserByUsernameAndPassword(@Param("name") String name, @Param("password") String password);
}
