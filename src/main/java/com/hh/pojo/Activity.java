package com.hh.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Activity {
    private int activityId;
    private int organizerId;
    private String name;
    private String location;
    private int courtId;
    private Date startTime;
    private Date endTime;

}
