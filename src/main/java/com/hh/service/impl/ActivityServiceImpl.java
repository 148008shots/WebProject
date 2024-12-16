package com.hh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hh.mapper.ActivityMapper;
import com.hh.pojo.Activity;
import com.hh.pojo.PageBean;
import com.hh.pojo.Registration;
import com.hh.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<Activity> getAllActivities() {
        return activityMapper.getAllActivities();
    }

    @Override
    public int insertActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public int deleteActivityById(int activityId) {
        return activityMapper.deleteActivityById(activityId);
    }

    @Override
    public int updateActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public PageBean<Activity> getAllActivities1(Integer pageNum, Integer pageSize) {
        PageBean<Activity> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> ac = activityMapper.getAllActivities();
        Page<Activity> p = (Page<Activity>) ac;

        pb.setItems(p.getResult());
        pb.setTotal(p.getTotal());
        return pb;
    }

    @Override
    public PageBean<Activity> getUserActivities(Integer pageNum, Integer pageSize, Integer userId) {
        PageBean<Activity> pb = new PageBean<>();
        PageHelper.startPage(pageNum, pageSize);
        List<Activity> ac = activityMapper.selectActivityByUserId(userId);
        Page<Activity> p = (Page<Activity>) ac;

        pb.setItems(p.getResult());
        pb.setTotal(p.getTotal());
        return pb;
    }

    @Override
    public void joinActivity(Integer userId, Integer activityId) {
        activityMapper.joinActivity(userId, activityId);
    }

    @Override
    public int increaseActivityMember(Integer activityId) {
        Integer nweMember = 1;
        return activityMapper.updateActivityMember(nweMember, activityId);
    }

    @Override
    public void leaveActivity(Integer userId, Integer activityId) {
        activityMapper.leaveActivity(userId, activityId);

    }

    @Override
    public List<Registration> selectRegistrationsByUserIdAndActivityId(Integer userId, Integer activityId) {
        return activityMapper.selectRegistrationsByUserIdAndActivityId(userId, activityId);
    }

    @Override
    public int decreaseActivityMember(Integer activityId) {
        Integer nweMember = 1;
        return activityMapper.updateActivityMember(-nweMember, activityId);
    }

    @Override
    public List<Activity> getActivityByUserCreateId(Integer id) {
        return activityMapper.selectActivityByUserIdCreate(id);
    }

}
