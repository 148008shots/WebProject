package com.hh.controller;

import com.hh.pojo.Bookings;
import com.hh.pojo.Result;
import com.hh.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Booking")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/getById/{id}")
    public Result<List<Bookings>> getBooksById(@PathVariable Integer id) {
        List<Bookings> bookingsList = bookingService.getBooksById(id);
        return Result.success(bookingsList);
    }
}
