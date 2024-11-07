package com.hh.controller;

import com.hh.pojo.Result;
import com.hh.pojo.User;
import com.hh.service.UserService;
import com.hh.utils.JwtUtil;
import com.hh.utils.Md5Util;
import com.hh.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$")String username,@Pattern(regexp = "^\\S{5,16}$")String password,@Pattern(regexp = "^1[3-9]\\d{9}$") String phone){
        //查询用户
        User u =userService.findByUserName(username);
        //判断用户是否为空
        if (u == null){
            userService.register(username,password,phone);
            return Result.success();
        }else {
            //非空不合法，被占用
            return Result.error("被占用");
        }
    }
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$")String username,@Pattern(regexp = "^\\S{5,16}$")String password){
        //查询用户
        User loginUser = userService.findByUserName(username);
        //判断用户名是否正确
        if(loginUser==null){
            return Result.error("用户名错误！");
        }
        //判断密码是否正确
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            //登陆成功
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            //
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误！");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name = "Author") String token*/){
/*        Map<String, Object> map = JwtUtil.parseToken(token);
        String  username = (String) map.get("username");*/
        Map<String ,Object> map = ThreadLocalUtil.get();
        String  username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String ,String> params,@RequestHeader("Authorization")String token){
        String old_pwd = params.get("old_pwd");
        String new_pwd = params.get("new_pwd");
        String re_pwd = params.get("re_pwd");
        if (!StringUtils.hasLength(old_pwd) ||!StringUtils.hasLength(new_pwd) ||!StringUtils.hasLength(re_pwd)){
            return Result.error("缺少必要参数！");
        }
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(old_pwd))){
            return Result.error("原密码不正确！");
        }

        if (!re_pwd.equals(new_pwd)){
            return Result.error("新密码不一致！");
        }

        userService.updatePwd(new_pwd);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }

    @GetMapping("/userList")
    public Result  userList(){
        List<User> list=  userService.fetchAllUsers();
        return Result.success(list);
    }

    @PatchMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody @Validated User user){
        userService.updateUser(user);
        return Result.success();
    }

    @DeleteMapping("/delete")
    public Result deleteUser(Integer id){
        System.out.println(id);
        userService.deleteUser(id);
        return Result.success();
    }
}

