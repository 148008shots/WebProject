package com.hh.service;

import com.hh.pojo.Activity;
import com.hh.pojo.PageBean;

import java.util.List;

public interface ActivityService {

    List<Activity> getAllActivities();
    int insertActivity(Activity activity);
    int deleteActivityById(int activityId);
    int updateActivity(Activity activity);

    PageBean<Activity> getAllActivities1(Integer pageNum, Integer pageSize);
}

