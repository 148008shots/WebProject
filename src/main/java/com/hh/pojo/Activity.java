package com.hh.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Activity {
    private int activityId;
    private int organizerId;
    private int status;
    private int signedUpCount;
    private String name;
    private String location;
    private Integer CategoryId;
    private Date startTime;
    private Date endTime;
    private Date signUpDeadline;


}
