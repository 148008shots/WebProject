package com.webee.controller;

import com.webee.pojo.Project;
import com.webee.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProjectController {

    @Autowired
    private ProjectService ProjectService;

    //只能输出一行数据
    @RequestMapping("/project")
    public ModelAndView findById(Integer id){

        Project project = ProjectService.findById(1);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("projects/project");
        modelAndView.addObject("project",project);

        return modelAndView;
    }

  //  全部输出数据
    @RequestMapping("/projects")
    public ModelAndView listProjects() {
        List<Project> projects = ProjectService.findAllProjects(); // 假设这个方法返回所有项目

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/projects/projects"); // 注意这里的视图名称应该与您的JSP文件名相匹配
        modelAndView.addObject("projects", projects); // 传递整个列表到视图

        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/demo")
    public String demo(){
        return "success";
    }
}
