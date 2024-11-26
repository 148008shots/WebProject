package com.hh.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Announcement {
    private Integer id;
    private String title;
    private String summary;
    private String content;
    private LocalDateTime publishTime;
}
