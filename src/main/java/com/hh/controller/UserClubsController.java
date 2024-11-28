package com.hh.controller;


import com.hh.pojo.Clubs;
import com.hh.pojo.Result;
import com.hh.pojo.UserClubs;
import com.hh.service.UserClubsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("userClub")
public class UserClubsController {

    private final UserClubsService userClubsService;

    public UserClubsController(UserClubsService userClubsService) {
        this.userClubsService = userClubsService;
    }

    @GetMapping("/fetchUserClubs/{userId}")
    public Result<List<UserClubs>> fetchUserClubs(@PathVariable int userId) {
        List<UserClubs> userClubs = userClubsService.selectClubByUserId(userId);
        return Result.success(userClubs);
    }

    @GetMapping("/fetchUserClubs1/{userId}")
    public Result<List<Clubs>> fetchUserClubsByUserId(@PathVariable int userId) {
        List<Clubs> userClubs = userClubsService.selectClubByUserId1(userId);
        return Result.success(userClubs);
    }


    @PostMapping("joinClub")
    public Result<String> joinClub(
            @RequestParam Integer userId,
            @RequestParam Integer clubId,
            @RequestParam Integer operation
    ) {
        userClubsService.joinClub(userId, clubId);
        return Result.success("成功加入社团");
    }

    @PostMapping("/updateUserClub")
    public Result<String> updateUserClub(
            @RequestParam Integer userId,
            @RequestParam Integer clubId,
            @RequestParam Integer operation) {
        Integer resultCode = null; // 用于存储操作结果的状态码
        String resultMessage = null; // 用于存储操作结果的消息

        List<UserClubs> memberList = userClubsService.selectUserClubsByUserIdAndClubId(userId, clubId);
        boolean isMember = memberList != null && !memberList.isEmpty();

        try {
            if (operation != null) {
                switch (operation) {
                    case 0: // 加入社团
                        if (isMember) {
                            // 如果已是成员，则返回失败结果
                            resultCode = 1;
                            resultMessage = "您已经是这个社团的成员";
                        } else {
                            // 如果不是成员，则加入社团
                            userClubsService.joinClub(userId, clubId);
                            userClubsService.increaseClubMember(clubId);
                            resultCode = 0; // 成功
                            resultMessage = "成功加入社团";
                        }
                        break;
                    case 1: // 退出社团
                        if (isMember) {
                            // 如果是成员，则退出社团
                            userClubsService.leaveClub(userId, clubId);
                            userClubsService.decreaseClubMember(clubId);
                            resultCode = 0; // 成功
                            resultMessage = "成功退出社团";
                        } else {
                            // 如果不是成员，则返回失败结果
                            resultCode = 1;
                            resultMessage = "您不是这个社团的成员，无法退出";
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
