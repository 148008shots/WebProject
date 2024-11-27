package com.hh.service;

import com.hh.pojo.Activity;
import com.hh.pojo.PageBean;
import com.hh.pojo.Registration;

import java.util.List;

public interface ActivityService {

    List<Activity> getAllActivities();

    int insertActivity(Activity activity);

    int deleteActivityById(int activityId);

    int updateActivity(Activity activity);

    PageBean<Activity> getAllActivities1(Integer pageNum, Integer pageSize);

    PageBean<Activity> getUserActivities(Integer pageNum, Integer pageSize, Integer userId);

    void joinActivity(Integer userId, Integer activityId);

    int increaseClubMember(Integer activityId);

    void leaveActivity(Integer userId, Integer activityId);

    List<Registration> selectRegistrationsByUserIdAndActivityId(Integer userId, Integer activityId);

    int decreaseClubMember(Integer activityId);

    List<Activity> getActivityByUserCreateId(Integer id);
}

