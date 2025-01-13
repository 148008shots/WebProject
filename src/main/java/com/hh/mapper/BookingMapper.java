package com.hh.mapper;

import com.hh.pojo.Bookings;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookingMapper {

    @Select("SELECT * FROM bookings WHERE user_id=#{id}")
    List<Bookings> findAppointmentsByDate(Integer id);

    @Insert("INSERT INTO bookings (user_id, court_id, start_time, end_time, day_of_year, day_of_month, day, created_at) VALUES " +
            "(#{userId}, #{courtId}, #{startTime}, #{endTime}, #{dayOfYear}, #{dayOfMonth}, #{day}, #{createdAt})")
    void insertBooking(Bookings booking);

    @Select({
            "SELECT ",
            "b.booking_id,",
            "b.user_id,",
            "b.court_id,",
            "b.start_time,",
            "b.end_time,",
            "b.created_at,",
            "b.status,",
            "b.day,",
            "b.day_of_month,",
            "b.day_of_year,",
            "c.court_number,",
            "c.location,",
            "c.cover_img,",
            "cat.name AS category_name",
            "FROM bookings b",
            "JOIN courts c ON b.court_id = c.court_id",
            "JOIN categories cat ON c.category_id = cat.category_id",
            "WHERE b.user_id = #{userId}",
            "ORDER BY b.created_at DESC"
    })
    List<Bookings> findBookingsByUserId(@Param("userId") int userId);

    @Select({
            "SELECT ",
            "b.booking_id,",
            "b.user_id,",
            "u.username AS username,",  // 添加这一行来选择用户名
            "b.court_id,",
            "b.start_time,",
            "b.end_time,",
            "b.created_at,",
            "b.day,",
            "b.day_of_month,",
            "b.day_of_year,",
            "c.court_number,",
            "c.location,",
            "c.cover_img,",
            "cat.name AS category_name",
            "FROM bookings b",
            "JOIN courts c ON b.court_id = c.court_id",
            "JOIN categories cat ON c.category_id = cat.category_id",
            "JOIN user u ON b.user_id = u.id",  // 添加这一行来关联用户表
            "ORDER BY b.created_at DESC"
    })
    List<Bookings> findAllBookings();

    @Update("UPDATE bookings SET status = 1 WHERE booking_id = #{reservationId}")
    void updateBookingStatus(Integer reservationId);
}
