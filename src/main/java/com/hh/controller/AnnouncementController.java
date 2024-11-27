package com.hh.controller;

import com.hh.pojo.Announcement;
import com.hh.pojo.Result;
import com.hh.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//公告
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    @Autowired
    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping("/getAnnouncement")
    public Result<Announcement> getAnnouncement(
    ) {
        Announcement announcement = announcementService.selectLastAnnouncementByPublishTime();
        return Result.success(announcement);

    }

    @GetMapping("/getAllAnnouncement")
    public Result<List<Announcement>> getAllAnnouncement(
    ) {
        List<Announcement> announcementList = announcementService.selectAnnouncement();
        return Result.success(announcementList);

    }

    @PostMapping("/update")
    private Result<?> updateAnnouncement(@RequestBody Announcement announcement) {
        announcementService.updateAnnouncement(announcement);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    private Result<?> deleteAnnouncement(@PathVariable int id) {
        announcementService.deleteAnnouncement(id);
        return Result.success();
    }

    @PostMapping("/add")
    private Result<?> addAnnouncement(@RequestBody Announcement announcement) {
        announcementService.addAnnouncement(announcement);
        return Result.success();
    }
}
