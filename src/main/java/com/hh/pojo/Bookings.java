package com.hh.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Bookings {
    private Integer bookingId;
    private Integer userId;
    private Integer courtId;
    private String startTime;
    private String endTime;
    private LocalDateTime createdAt;
    private Integer dayOfYear;
    private Integer dayOfMonth;
    private Integer day;
    private String date;
}
