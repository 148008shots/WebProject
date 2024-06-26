package com.webee.controller;

import com.webee.pojo.Users;
import com.webee.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("user/login.jsp");
        // 如果需要，添加登录失败的错误消息
        modelAndView.addObject("loginError", "Username or password is incorrect.");
        return modelAndView;
    }


    @PostMapping("/login")
    public ModelAndView authenticateUser(@RequestParam("name") String name,
                                         @RequestParam("password") String password,
                                         @RequestParam("role") String role,
                                         ModelAndView modelAndView,
                                         HttpServletRequest request) {
        // 这里添加你的验证逻辑，例如查询数据库
        Users isValid= usersService.findUserByUsernameAndPassword(name,password);
        //判断选择输入的是管理员还是用户
        boolean isUser = "user".equals(role);
        boolean isAdmin = "admin".equals(role);

        if (isValid != null) {

            HttpSession session = request.getSession();
            session.setAttribute("USER_SESSION",isValid);
            // 登录成功，重定向到主页
            if(isAdmin){
                modelAndView.setViewName("admin/Dashboard.jsp");
            }
            else if (isUser){
                modelAndView.setViewName("user/Dashboard.jsp");
            }
        } else {
            // 登录失败，返回登录页面并显示错误消息
            modelAndView.setViewName("user/login.jsp");
            modelAndView.addObject("loginError", "用户名或密码错误！");
        }
        return modelAndView;
    }



    // 在控制器中返回到注册页面表单
    @RequestMapping("/goRegister")
    public String goRegister() {
        return "user/register.jsp";
    }

    //接收页面表单提交
    @RequestMapping("/register")
    public String register(Users user, HttpServletRequest request) {

        // 这里添加用户注册逻辑，包括邮箱验证
        boolean isUserSaved = usersService.saveUser(user);
        if (isUserSaved) {
            // 注册成功，重定向到登录页面或其他页面
            return "user/login.jsp";
        } else {
            // 注册失败，返回注册页面并显示错误消息
            request.setAttribute("registerError", "Registration failed. Please try again.");
            return "user/register.jsp"; // 回到注册页面
        }
    }
}
