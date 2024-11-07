package com.hh.controller;

import com.hh.pojo.Activity;
import com.hh.pojo.PageBean;
import com.hh.pojo.Result;
import com.hh.service.ActivityService;
import com.hh.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping("/getAllActivities")
    public Result<PageBean<Activity>> getAllActivities(
            Integer pageNum,
            Integer pageSize
    ) {
        PageBean<Activity> pageBean = activityService.getAllActivities1(pageNum,pageSize);
        return Result.success(pageBean);
    }

    @PostMapping("/addActivity")
    public Result<String> addActivity(@RequestBody Activity activity) {
        Map<String ,Object> map = ThreadLocalUtil.get();
        int id = (int) map.get("id");
        activity.setOrganizerId(id);
        int result = activityService.insertActivity(activity);
        if (result > 0) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    @DeleteMapping("/deleteActivity/{activityId}")
    public Result<String> deleteActivity(@PathVariable int activityId) {
        int result = activityService.deleteActivityById(activityId);
        if (result > 0) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    @PutMapping("/updateActivity")
    public Result<String> updateActivity(@RequestBody Activity activity) {
        int result = activityService.updateActivity(activity);
        if (result > 0) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }
}
