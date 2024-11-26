package com.hh.service.impl;

import com.hh.mapper.AnnouncementMapper;
import com.hh.pojo.Announcement;
import com.hh.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    private final AnnouncementMapper announcementMapper;

    @Autowired
    public AnnouncementServiceImpl(AnnouncementMapper announcementMapper) {
        this.announcementMapper = announcementMapper;
    }

    @Override
    public Announcement selectLastAnnouncementByPublishTime() {
        return announcementMapper.selectLastAnnouncementByPublishTime();
    }

    @Override
    public List<Announcement> selectAnnouncement() {
        return announcementMapper.selectAllAnnouncements();
    }

    @Override
    public int updateAnnouncement(Announcement announcement) {
        return announcementMapper.updateAnnouncement(announcement);
    }

    @Override
    public int deleteAnnouncement(int id) {
        return announcementMapper.deleteAnnouncementById(id);
    }

    @Override
    public int addAnnouncement(Announcement announcement) {
        return announcementMapper.insertAnnouncement(announcement);
    }
}
