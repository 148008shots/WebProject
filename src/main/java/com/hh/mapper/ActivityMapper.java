package com.hh.mapper;

import com.hh.pojo.Activity;
import com.hh.pojo.Registration;
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

    @Select("SELECT * FROM registrations")
    List<Registration> getAllRegistrations();

    @Select("SELECT * FROM registrations WHERE activity_id = #{activityId} AND user_id = #{userId}")
    List<Registration> selectRegistrationsByUserIdAndActivityId(Integer userId, Integer activityId);

    @Insert("INSERT INTO registrations (user_id, activity_id) VALUES (#{userId}, #{activityId})")
    void joinActivity(Integer userId, Integer activityId);

    @Update("UPDATE activities SET signed_up_count = signed_up_count + #{nweMember} WHERE activity_id = #{activityId}")
    int updateActivityMember(Integer nweMember, Integer activityId);

    @Delete("DELETE FROM registrations WHERE activity_id = #{activityId} AND user_id = #{userId}")
    void leaveActivity(Integer userId, Integer activityId);
}
