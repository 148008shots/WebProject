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

    @Select("SELECT " +
            "r.registration_id, " +
            "r.user_id, " +
            "a.activity_id, " +
            "r.registrations_status, " +
            "a.name, " +
            "a.location, " +
            "a.court_id, " +
            "a.start_time, " +
            "a.end_time, " +
            "a.status, " +
            "a.signed_up_count, " +
            "a.sign_up_deadline " +
            "FROM activities a " +
            "JOIN registrations r ON a.activity_id = r.activity_id " +
            "WHERE r.user_id = #{userId}")
    List<Activity> selectActivityByUserId(Integer userId);

    @Select("SELECT * FROM registrations WHERE activity_id = #{activityId} AND user_id = #{userId}")
    List<Registration> selectRegistrationsByUserIdAndActivityId(Integer userId, Integer activityId);

    //    查询记录
    @Select("SELECT * FROM registrations WHERE user_id = #{userId} AND registrations_status = 0 ")
    List<Registration> selectRegistrationsByUserId(Integer userId);

    @Insert("INSERT INTO registrations (user_id, activity_id) VALUES (#{userId}, #{activityId})")
    void joinActivity(Integer userId, Integer activityId);

    @Update("UPDATE activities SET signed_up_count = signed_up_count + #{nweMember} WHERE activity_id = #{activityId}")
    int updateActivityMember(Integer nweMember, Integer activityId);

    @Update("UPDATE registrations SET registrations_status = 1 WHERE activity_id = #{activityId} AND user_id = #{userId}")
    void leaveActivity(Integer userId, Integer activityId);
}
