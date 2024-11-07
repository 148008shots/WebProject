package com.hh.controller;

import com.hh.pojo.Court;
import com.hh.pojo.PageBean;
import com.hh.pojo.Result;
import com.hh.service.CourtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/court")
public class CourtsController {
    @Autowired
    private CourtsService courtsService;

    @GetMapping
    public Result<PageBean<Court>>  listCourts(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer courtId,
            @RequestParam(required = false) String courtNumber
    ) {
        // 调用服务层方法获取球场信息列表
        PageBean<Court> pageBean= courtsService.list1(pageNum,pageSize,courtId,courtNumber);
        return Result.success(pageBean);
    }
    @PostMapping
    public Result addCourt(@RequestBody Court court) {
        courtsService.insertCourt(court);
        return Result.success();
    }
    @PutMapping
    public Result updateCourt(@RequestBody Court court) {
        courtsService.updateCourt(court);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteCourt(Integer courtId) {
        courtsService.deleteCourt(courtId);
        return Result.success();
    }
}
