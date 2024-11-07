package com.hh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hh.mapper.ActivityMapper;
import com.hh.pojo.Activity;
import com.hh.pojo.PageBean;
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
        PageHelper.startPage(pageNum,pageSize);
        List<Activity> ac = activityMapper.getAllActivities();
        Page<Activity> p = (Page<Activity>) ac;

        pb.setItems(p.getResult());
        pb.setTotal(p.getTotal());
        return pb;
    }

}
