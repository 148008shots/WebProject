package com.hh.mapper;

import com.hh.pojo.Activity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ActivityMapper {
    @Insert("INSERT INTO activities (organizer_id, name, location, court_id, start_time, end_time) VALUES (#{organizerId}, #{name}, #{location}, #{courtId}, #{startTime}, #{endTime})")
    int insertActivity(Activity activity);

    @Delete("DELETE FROM activities WHERE activity_id = #{activityId}")
    int deleteActivityById(int activityId);

    @Update("UPDATE activities SET organizer_id = #{organizerId}, name = #{name}, location = #{location}, court_id = #{courtId}, start_time = #{startTime}, end_time = #{endTime} WHERE activity_id = #{activityId}")
    int updateActivity(Activity activity);

    @Select("SELECT * FROM activities")
    List<Activity> getAllActivities();
}
