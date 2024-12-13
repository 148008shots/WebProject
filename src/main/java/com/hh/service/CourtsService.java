package com.hh.service;

import com.hh.pojo.Bookings;
import com.hh.pojo.Court;
import com.hh.pojo.PageBean;
import com.hh.pojo.TimeSlots;

import java.util.List;

public interface CourtsService {
    List<Court> getAllCourts();

    void insertCourt(Court court);
    int updateCourt(Court court);
    int deleteCourt(Integer courtId);

    PageBean<Court> list1(Integer pageNum, Integer pageSize, Integer categoryId, String courtNumber);

    List<TimeSlots> getAllTimeSlot();
    List<TimeSlots> getAllTimeSlotByCourtId(Integer courtId,String date);


    List<Bookings> findAppointmentsByDate(String date);
}
