package com.hh.mapper;

import com.hh.pojo.Clubs;
import com.hh.pojo.UserClubs;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClubsMapper {

    @Insert("INSERT INTO clubs (category_id, name, description, address, contact_user_id) VALUES (#{categoryId}, #{name}, #{description}, #{address}, #{contactUserId})")
    int insertClub(Clubs club);

    @Update("UPDATE clubs SET category_id = #{categoryId}, name = #{name}, description = #{description}, address = #{address}, contact_user_id = #{contactUserId} WHERE club_id = #{clubId}")
    int updateClubById(Clubs club);

    @Select("SELECT * FROM clubs")
    List<Clubs> selectAllClubs();

    @Delete("DELETE FROM clubs WHERE club_id = #{clubId}")
    int deleteClubById( int clubId);

    @Insert("INSERT INTO userclubs (user_id, club_id) VALUES (#{userId}, #{clubId})")
    void joinClub(Integer userId, Integer clubId);

    @Select("SELECT * FROM userclubs WHERE user_id = #{userId}")
    List<UserClubs> selectClubByUserId(int userId);

    @Update("UPDATE clubs SET members = members + #{nweMember} WHERE club_id = #{clubId}")
    int updateClubMember(Integer nweMember,Integer clubId);

    List<Clubs> selectClubByUserId1(Integer userId);
}
