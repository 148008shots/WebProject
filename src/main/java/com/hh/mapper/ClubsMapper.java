package com.hh.mapper;

import com.hh.pojo.Clubs;
import com.hh.pojo.UserClubs;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClubsMapper {

    @Insert("INSERT INTO clubs (category_id, name, description, address, contact_user_id,clubs_pic) VALUES (#{categoryId}, #{name}, #{description}, #{address}, #{contactUserId},#{clubsPic})")
    int insertClub(Clubs club);

    @Update("UPDATE clubs SET category_id = #{categoryId}, name = #{name}, description = #{description}, address = #{address}, contact_user_id = #{contactUserId}, clubs_pic = #{clubsPic} WHERE club_id = #{clubId}")
    int updateClubById(Clubs club);

    @Select("SELECT * FROM clubs")
    List<Clubs> selectAllClubs();

    @Delete("DELETE FROM clubs WHERE club_id = #{clubId}")
    int deleteClubById( int clubId);

}
