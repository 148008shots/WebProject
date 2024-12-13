package com.hh.controller;

import com.hh.pojo.*;
import com.hh.service.CourtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/court")
public class CourtsController {
    @Autowired
    private CourtsService courtsService;

    @GetMapping
    public Result<PageBean<Court>> listCourts(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) Integer categoryId,//分类id
            @RequestParam(required = false) String courtName // 场地名称
    ) {
        // 调用服务层方法获取球场信息列表
        PageBean<Court> pageBean = courtsService.list1(pageNum, pageSize, categoryId, courtName);
        return Result.success(pageBean);
    }

    @GetMapping("/allCourts")
    public Result<List<Court>> allCourts(
    ) {
        // 调用服务层方法获取球场信息列表
        List<Court> courtList = courtsService.getAllCourts();
        return Result.success(courtList);
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

    @GetMapping("/timeSlots")
    public Result getTimeSlot(){
       List<TimeSlots> times =courtsService.getAllTimeSlot();
       return Result.success(times);
    }

    @GetMapping("/timeSlotsByCourtId")
    public Result getTimeByCourtId(Integer courtId,String date){
        List<TimeSlots> times = courtsService.getAllTimeSlotByCourtId(courtId, date);
        return Result.success(times);
    }


    // 获取当天预约情况的API端点
    @GetMapping("/today-appointments")
    public Result getTodayAppointments(@RequestParam("date") String date) {
        // 调用服务层方法，传入日期参数
        List<Bookings> appointmentsByDate = courtsService.findAppointmentsByDate(date);
        return Result.success(appointmentsByDate);
    }
}
