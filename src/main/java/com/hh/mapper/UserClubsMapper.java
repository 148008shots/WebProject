package com.hh.mapper;

import com.hh.pojo.Clubs;
import com.hh.pojo.UserClubs;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserClubsMapper {
    @Insert("INSERT INTO userclubs (user_id, club_id) VALUES (#{userId}, #{clubId})")
    void joinClub(Integer userId, Integer clubId);


    @Select("SELECT * FROM userclubs WHERE club_id = #{clubId} AND user_id = #{userId}")
    List<UserClubs> selectUserClubsByUserIdAndClubId(Integer userId, Integer clubId);

    @Update("UPDATE clubs SET members = members + #{nweMember} WHERE club_id = #{clubId}")
    int updateClubMember(Integer nweMember, Integer clubId);

    @Delete("DELETE FROM userclubs WHERE club_id = #{clubId} AND user_id = #{userId}")
    void leaveClub(Integer userId, Integer clubId);

    @Select("SELECT * FROM userclubs WHERE user_id = #{userId}")
    List<UserClubs> selectClubByUserId(int userId);

    List<Clubs> selectClubByUserId1(Integer userId);
}
