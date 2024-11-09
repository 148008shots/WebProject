package com.hh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hh.mapper.CourtMapper;
import com.hh.pojo.Bookings;
import com.hh.pojo.Court;
import com.hh.pojo.PageBean;
import com.hh.pojo.TimeSlots;
import com.hh.service.CourtsService;
import com.hh.utils.TimeForUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CourtsServiceImpl implements CourtsService {

    @Autowired
    private CourtMapper courtMapper;
    @Override
    public List<Court> getAllCourts() {
        return courtMapper.selectAllCourts();
    }

    @Override
    public void insertCourt(Court court) {
         courtMapper.insertCourt(court);
    }

    @Override
    public int updateCourt(Court court) {
        return courtMapper.updateCourt(court);
    }

    @Override
    public int deleteCourt(Integer courtId) {
        return courtMapper.deleteCourt(courtId);
    }

    @Override
    public PageBean<Court> list1(Integer pageNum, Integer pageSize, Integer courtId, String courtNumber) {
        PageBean<Court> pb = new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        List<Court> cs = courtMapper.select1(courtId, courtNumber);

        Page<Court> p = (Page<Court>) cs;

        pb.setItems(p.getResult());
        pb.setTotal(p.getTotal());
        return pb;
    }

    @Override
    public List<TimeSlots> getAllTimeSlot() {
        return courtMapper.selectTimeSlot();
    }

    @Override
    public List<TimeSlots> getAllTimeSlotByCourtId(Integer courtId, String date) {
        // 1. 查询所有时间段
        List<TimeSlots> allTimeSlots = getAllTimeSlot();
        // 格式化日期
        int[] dateParts = TimeForUtil.parseDate1(date);
        // 2. 根据场地Id查询和日期预约信息
        List<Bookings> bos = courtMapper.selectBookingsByCourtIdAndDate(courtId, dateParts[0], dateParts[1], dateParts[2]);
        // 3. 遍历时间段和预约信息，更新时间段状态
        for (TimeSlots timeSlot : allTimeSlots) {
            for (Bookings booking : bos) {
                if (isTimeOverlap(timeSlot.getTime(), booking.getStartTime(), booking.getEndTime())) {
                    timeSlot.setStatus(1);
                    break; // 如果已经找到重叠，就不需要继续检查其他预约
                }
            }
        }
        // 4. 将设置好的时间段返回到控制层去
        return allTimeSlots;
    }
    private boolean isTimeOverlap(String timeSlotTime, String startTime, String endTime) {
        // 将字符串时间转换为LocalTime对象
        LocalTime timeSlotLocalTime = TimeForUtil.parseTime(timeSlotTime);
        LocalTime startLocalTime = TimeForUtil.parseTime(startTime);
        LocalTime endLocalTime = TimeForUtil.parseTime(endTime);

        // 检查时间段是否在预约时间内
        return !timeSlotLocalTime.isBefore(startLocalTime) && !timeSlotLocalTime.isAfter(endLocalTime);
    }


    public void createBooking(Bookings booking) {
        int[] dateParts = TimeForUtil.parseDate1(booking.getDate());
        booking.setDayOfYear(dateParts[0]);
        booking.setDayOfMonth(dateParts[1]);
        booking.setDay(dateParts[2]);
        booking.setCreatedAt(LocalDateTime.now());
        System.out.println(booking);
        courtMapper.insertBooking(booking);
    }


}
