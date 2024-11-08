package com.hh.mapper;

import com.hh.pojo.Bookings;
import com.hh.pojo.Court;
import com.hh.pojo.TimeSlots;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CourtMapper {
    List<Court> selectAllCourts();

    List<Court> select1(Integer courtId,String courtNumber);
    @Insert("INSERT INTO courts (category_id, location, court_number, cover_img) VALUES (#{categoryId}, #{location}, #{courtNumber}, #{coverImg})")
    void insertCourt( Court court);

    @Update("UPDATE courts SET  location = #{location},category_id = #{categoryId}, court_number = #{courtNumber}, cover_img = #{coverImg} WHERE court_id = #{courtId}")
    int updateCourt(Court court);

    @Delete("DELETE FROM courts WHERE court_id = #{courtId}")
    int deleteCourt(@Param("courtId") int courtId);

    @Select("select * from time_slots")
    List<TimeSlots> selectTimeSlot();
    @Select("select * from bookings where court_id = #{courtId}")
    List<Bookings> selectBookingByCourtId(@Param("courtId") int courtId);

}
