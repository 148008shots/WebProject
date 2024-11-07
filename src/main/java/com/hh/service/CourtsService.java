package com.hh.service;

import com.hh.pojo.Court;
import com.hh.pojo.PageBean;

import java.util.List;

public interface CourtsService {
    List<Court> getAllCourts();

    void insertCourt(Court court);
    int updateCourt(Court court);
    int deleteCourt(Integer courtId);

    PageBean<Court> list1(Integer pageNum, Integer pageSize, Integer courtId, String courtNumber);
}
