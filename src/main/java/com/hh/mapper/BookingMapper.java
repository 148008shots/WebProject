package com.hh.mapper;

import com.hh.pojo.Bookings;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
}
