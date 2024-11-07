package com.hh.pojo;

import lombok.Data;

@Data
public class Clubs {
    private int clubId;
    private int categoryId;
    private String name;
    private String description;
    private String address;
    private int contactUserId;
}
