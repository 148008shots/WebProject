package com.hh.service;

import com.hh.pojo.Bookings;

import java.util.List;

public interface BookingService {
    List<Bookings> getBooksById(Integer id);
}
