package com.hh.service.impl;

import com.hh.mapper.BookingMapper;
import com.hh.pojo.Bookings;
import com.hh.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingMapper bookingMapper;

    @Override
    public List<Bookings> getBooksById(Integer id) {
        return bookingMapper.findAppointmentsByDate(id);
    }
}
