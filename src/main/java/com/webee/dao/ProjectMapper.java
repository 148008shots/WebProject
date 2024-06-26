package com.webee.dao;

import com.webee.pojo.Project;

import java.util.List;

public interface ProjectMapper {
    public Project findById(Integer id);

    List<Project> findAllProjects();
}
