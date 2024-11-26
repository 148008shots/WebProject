package com.hh.service;

import com.hh.pojo.Announcement;

import java.util.List;

public interface AnnouncementService {
    Announcement selectLastAnnouncementByPublishTime();

    List<Announcement> selectAnnouncement();

    int updateAnnouncement(Announcement announcement);

    int deleteAnnouncement(int id);

    int addAnnouncement(Announcement announcement);
}
