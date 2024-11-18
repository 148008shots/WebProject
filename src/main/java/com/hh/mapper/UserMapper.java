package com.hh.mapper;

import com.hh.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据用户名查询用户
    @Select("select * from user where username = #{username}")
    User findByUserName(String username);
    //添加注册用户
    @Insert("insert into user(username,phone,password,create_time,update_time)"+
            " values(#{username},#{phone},#{password},now(),now())")
    void add(String username, String password,String phone);
    @Update("update user set phone=#{phone},nickname=#{nickname},email=#{email},update_time=#{updateTime} where id = #{id}")
    void update(User user);

    @Update("update user set user_pic=#{avatarUrl},update_time=now() where id = #{id}")
    void updateAvatar(String avatarUrl,Integer id);
    @Update("update user set password=#{md5String},update_time=now() where id = #{id}")
    void updatePwd(String md5String, Integer id);

    @Delete("delete from user where id = #{id}")
    void deleteUserByUsername(Integer id);

    @Select("SELECT * FROM user")
    List<User> findAllUsers();

    @Update("UPDATE user SET username = #{username}, nickname = #{nickname}, email = #{email}, phone = #{phone}, role = #{role}, update_time = NOW() WHERE id = #{id}")
    void updateInfo(User user);

    @Select("select * from user where username = #{username} AND phone = #{phone}")
    User selectByUserNameAndPhone(String username, String phone);

    @Update("update user set password=#{md5String},update_time=now() where username = #{username}")
    void resetUpdatePwd(String md5String, String username);

    @Select("select * from user where phone = #{phone}")
    User findByUserPhone(String phone);
}
