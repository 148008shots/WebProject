package com.hh.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimeForUtil {
    // 自定义时间格式化器，用于解析HH:mm:ss格式的时间
    private static final DateTimeFormatter CUSTOM_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("HH:mm:ss")
            .toFormatter();

    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, CUSTOM_TIME_FORMATTER);
    }
}
