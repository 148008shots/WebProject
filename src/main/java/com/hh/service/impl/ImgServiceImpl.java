package com.hh.service.impl;

import com.hh.mapper.ImgMapper;
import com.hh.pojo.Img;
import com.hh.service.ImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service
public class ImgServiceImpl implements ImgService {

    @Autowired
    ImgMapper imgMapper;

    @Override
    public void save(Img img) {
        imgMapper.save(img);
    }
}
