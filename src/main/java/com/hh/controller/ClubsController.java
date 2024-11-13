package com.hh.controller;

import com.hh.pojo.Clubs;
import com.hh.pojo.UserClubs;
import com.hh.service.ClubsService;
import com.hh.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
public class ClubsController {
    private final ClubsService clubsService;
    @Autowired
    public ClubsController(ClubsService clubsService) {
        this.clubsService = clubsService;
    }

    @GetMapping("/getAllClubs")
    public Result<List<Clubs>> getAllClubs() {
        List<Clubs> clubs = clubsService.getAllClubs();
        return Result.success(clubs);
    }

    @PostMapping("/addClub")
    public Result<String> addClub(@RequestBody Clubs club) {
        clubsService.addClub(club);
        return Result.success("添加成功");
    }

    @DeleteMapping("/deleteClub/{clubId}")
    public Result<String> deleteClub(@PathVariable int clubId) {
        clubsService.deleteClubById(clubId);
        return Result.success("删除成功");
    }

    @PutMapping("/updateClub")
    public Result<String> updateClub(@RequestBody Clubs club) {
        clubsService.updateClub(club);
        return Result.success("更新成功");
    }

    @PostMapping("/joinClub")
    public Result<String> joinClub(
            @RequestParam Integer userId,
            @RequestParam Integer clubId,
            @RequestParam Integer operation
    ) {
        clubsService.joinClub(userId,clubId);
        return Result.success("成功加入社团");
    }
    @GetMapping("/fetchUserClubs/{userId}")
    public Result<List<UserClubs>> fetchUserClubs(@PathVariable int userId) {
        List<UserClubs> userClubs =clubsService.selectClubByUserId(userId);
        return Result.success(userClubs);
    }
    @GetMapping("/fetchUserClubs1/{userId}")
    public Result<List<Clubs>> fetchUserClubsByUserId(@PathVariable int userId) {
        List<Clubs> userClubs =clubsService.selectClubByUserId1(userId);
        return Result.success(userClubs);
    }

    @PostMapping("/updateUserClub")
    public Result<String> updateUserClub(
            @RequestParam Integer userId,
            @RequestParam Integer clubId,
            @RequestParam Integer operation) {
        Integer resultCode = null; // 用于存储操作结果的状态码
        String resultMessage = null; // 用于存储操作结果的消息

        // 根据operation的值决定是执行加入还是退出社团的操作
        try {
            if (operation != null) {
                switch (operation) {
                    case 0: // 加入社团
                        clubsService.joinClub(userId, clubId);
                        clubsService.increaseClubMember(clubId);
                        resultCode = 0; // 成功
                        resultMessage = "成功加入社团";
                        break;
                    case 1: // 退出社团
                        clubsService.leaveClub(userId, clubId);
                        clubsService.decreaseClubMember(clubId);
                        resultCode = 0; // 成功
                        resultMessage = "成功退出社团";
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