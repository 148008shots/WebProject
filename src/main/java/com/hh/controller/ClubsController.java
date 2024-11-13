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
            @RequestParam Integer clubId
    ) {
        System.out.println(userId);
        System.out.println(clubId);
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
}