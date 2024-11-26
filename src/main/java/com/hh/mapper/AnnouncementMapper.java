package com.hh.mapper;

import com.hh.pojo.Announcement;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AnnouncementMapper {

    // 增加公告
    @Insert("INSERT INTO announcements (title, summary, content, publish_time) " +
            "VALUES (#{title}, #{summary}, #{content}, #{publishTime})")
    int insertAnnouncement(Announcement announcement);

    // 根据ID删除公告
    @Delete("DELETE FROM announcements WHERE id = #{id}")
    int deleteAnnouncementById(int id);

    // 根据ID更新公告
    @Update("UPDATE announcements SET " +
            "title = #{title}, " +
            "summary = #{summary}, " +
            "content = #{content}, " +
            "publish_time = #{publishTime}" +
            "WHERE id = #{id}")
    int updateAnnouncement(Announcement announcement);

    // 根据ID查询公告
    @Select("SELECT * FROM announcements WHERE id = #{id}")
    Announcement selectAnnouncementById(int id);

    // 查询所有公告
    @Select("SELECT * FROM announcements")
    List<Announcement> selectAllAnnouncements();

    // 或者，如果以发布时间降序排列
    @Select("SELECT * FROM announcements ORDER BY publish_time DESC LIMIT 1")
    Announcement selectLastAnnouncementByPublishTime();
}
