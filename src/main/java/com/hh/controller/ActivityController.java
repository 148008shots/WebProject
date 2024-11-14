package com.hh.controller;

import com.hh.pojo.Activity;
import com.hh.pojo.PageBean;
import com.hh.pojo.Registration;
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

    @PostMapping("/signUpForActivity")
    public Result<String> signUpForActivity(
            @RequestParam Integer userId,
            @RequestParam Integer activityId,
            @RequestParam Integer operation) {
        Integer resultCode = null; // 用于存储操作结果的状态码
        String resultMessage = null; // 用于存储操作结果的消息

        // 根据传来的userId和activityId查找报名表里面是否存在相关记录
        List<Registration> registrationList = activityService.selectRegistrationsByUserIdAndActivityId(userId, activityId);
        boolean isRegistered = registrationList != null && !registrationList.isEmpty();

        try {
            if (operation != null) {
                switch (operation) {
                    case 0: // 加入活动
                        if (isRegistered) {
                            // 如果已报名，则返回失败结果
                            resultCode = 1;
                            resultMessage = "您已报名过这个活动";
                        } else {
                            // 如果未报名，则加入活动
                            activityService.joinActivity(userId, activityId);
                            activityService.increaseClubMember(activityId);
                            resultCode = 0; // 成功
                            resultMessage = "成功加入活动";
                        }
                        break;
                    case 1: // 退出活动
                        if (isRegistered) {
                            // 如果已报名，则退出活动
                            activityService.leaveActivity(userId, activityId);
                            activityService.decreaseClubMember(activityId);
                            resultCode = 0; // 成功
                            resultMessage = "成功退出活动";
                        } else {
                            // 如果未报名，则返回失败结果
                            resultCode = 1;
                            resultMessage = "您未报名过这个活动，无法退出";
                        }
                        break;
                    default:
                        resultCode = 1; // 失败
                        resultMessage = "无效的操作码";
                        break;
                }
            }
        } catch (Exception e) {
            // 捕获异常，设置失败的状态码和消息
            resultCode = 1; // 失败
            resultMessage = "操作失败: " + e.getMessage();
        }

        // 根据resultCode返回相应的Result对象
        return new Result<>(resultCode, resultMessage, null);
    }

}
