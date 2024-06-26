package com.webee.service.impl;

import com.webee.dao.ProjectMapper;
import com.webee.pojo.Project;
import com.webee.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper ProjectMapper;
    @Override
    public Project findById(Integer id) {
        return ProjectMapper.findById(id);
    }

    @Override
    public List<Project> findAllProjects() {
        return ProjectMapper.findAllProjects();
    }
}
