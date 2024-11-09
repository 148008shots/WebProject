package com.hh.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class TimeForUtil {
    // 自定义时间格式化器，用于解析HH:mm:ss格式的时间
    private static final DateTimeFormatter CUSTOM_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("HH:mm:ss")
            .toFormatter();

    // 自定义日期格式化器，用于解析yyyy-MM-dd格式的日期
    private static final DateTimeFormatter CUSTOM_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .toFormatter();

    // 自定义日期时间格式化器，用于解析ISO 8601格式的yyyy-MM-dd'T'HH:mm:ss.SSSX日期时间字符串
    private static final DateTimeFormatter CUSTOM_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
            .toFormatter();

    /**
     * 解析HH:mm:ss格式的时间字符串为LocalTime对象。
     *
     * @param time 时间字符串
     * @return LocalTime对象
     */
    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, CUSTOM_TIME_FORMATTER);
    }

    /**
     * 解析yyyy-MM-dd格式的日期字符串为LocalDate对象。
     *
     * @param date 日期字符串
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, CUSTOM_DATE_FORMATTER);
    }

    /**
     * 解析ISO 8601格式的yyyy-MM-dd'T'HH:mm:ss.SSSX日期时间字符串，并返回年月日数组。
     *
     * @param dateTimeStr 日期时间字符串
     * @return 包含年、月、日的int数组
     */
    public static int[] parseDateTime(String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, CUSTOM_DATE_TIME_FORMATTER);
        return new int[]{dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()};
    }
    public static int[] parseDate1(String date) {
        LocalDate localDate = LocalDate.parse(date, CUSTOM_DATE_FORMATTER);
        return new int[]{localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth()};
    }
}