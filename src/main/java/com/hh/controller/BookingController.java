package com.hh.controller;

import com.hh.pojo.Bookings;
import com.hh.pojo.Result;
import com.hh.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        List<Bookings> bookingsList = bookingService.findBookingsByUserId(id);
        return Result.success(bookingsList);
    }

    @PostMapping("/createBooking")
    public Result createBooking(@RequestBody Bookings booking) {
        System.out.println(booking);
        bookingService.createBooking(booking);
        return Result.success();
    }

    @GetMapping()
    public Result<List<Bookings>> getAllBooking() {
        List<Bookings> bookingsList = bookingService.findAllBookings();
        return Result.success(bookingsList);
    }
}