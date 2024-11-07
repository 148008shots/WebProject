package com.hh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hh.mapper.CourtMapper;
import com.hh.pojo.Court;
import com.hh.pojo.PageBean;
import com.hh.service.CourtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtsServiceImpl implements CourtsService {

    @Autowired
    private CourtMapper courtMapper;
    @Override
    public List<Court> getAllCourts() {
        return courtMapper.selectAllCourts();
    }

    @Override
    public void insertCourt(Court court) {
         courtMapper.insertCourt(court);
    }

    @Override
    public int updateCourt(Court court) {
        return courtMapper.updateCourt(court);
    }

    @Override
    public int deleteCourt(Integer courtId) {
        return courtMapper.deleteCourt(courtId);
    }

    @Override
    public PageBean<Court> list1(Integer pageNum, Integer pageSize, Integer courtId, String courtNumber) {
        PageBean<Court> pb = new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        List<Court> cs = courtMapper.select1(courtId, courtNumber);

        Page<Court> p = (Page<Court>) cs;

        pb.setItems(p.getResult());
        pb.setTotal(p.getTotal());
        return pb;
    }
}
