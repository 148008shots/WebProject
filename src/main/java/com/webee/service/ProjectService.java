package com.webee.service;

import com.webee.pojo.Project;

import java.util.List;

public interface ProjectService {
    public Project findById(Integer id);

    List<Project> findAllProjects();

}
