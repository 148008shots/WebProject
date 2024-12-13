package com.hh.service.impl;

import com.hh.mapper.BookingMapper;
import com.hh.pojo.Bookings;
import com.hh.service.BookingService;
import com.hh.utils.TimeForUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingMapper bookingMapper;

    @Override
    public List<Bookings> getBooksById(Integer id) {
        return bookingMapper.findAppointmentsByDate(id);
    }

    @Override
    public List<Bookings> findBookingsByUserId(Integer id) {
        return bookingMapper.findBookingsByUserId(id);
    }

    public void createBooking(Bookings booking) {
        int[] dateParts = TimeForUtil.parseDate1(booking.getDate());
        booking.setDayOfYear(dateParts[0]);
        booking.setDayOfMonth(dateParts[1]);
        booking.setDay(dateParts[2]);
        booking.setCreatedAt(LocalDateTime.now());
        System.out.println(booking);
        bookingMapper.insertBooking(booking);
    }
}
