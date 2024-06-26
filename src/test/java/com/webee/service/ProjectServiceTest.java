package com.webee.service;

import com.webee.pojo.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:application-service.xml","classpath:application-dao.xml"})
public class ProjectServiceTest {

    @Autowired
    private ProjectService ProjectService;
    @Test
    public void findById() {
        Project Project = ProjectService.findById(1);
        System.out.println(Project);
    }

    @Test
    public void testFindAllProjects() {
        List<Project> projects = ProjectService.findAllProjects();
        assertNotNull(projects); // 验证返回的列表不为空
        assertFalse(projects.isEmpty()); // 验证列表中至少有一个项目
        System.out.println(projects);
    }
}