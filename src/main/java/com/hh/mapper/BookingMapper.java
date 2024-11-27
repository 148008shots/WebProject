package com.hh.mapper;

import com.hh.pojo.Bookings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookingMapper {

    @Select("SELECT * FROM bookings WHERE user_id=#{id}")
    List<Bookings> findAppointmentsByDate(Integer id);
}
