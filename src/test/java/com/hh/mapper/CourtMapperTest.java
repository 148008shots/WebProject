package com.hh.mapper;

import com.hh.pojo.Court;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CourtMapperTest {

    @Autowired
    private CourtMapper courtMapper;

    @Test
    void selectAllCourts() {
        // 假设courts是CourtMapper.selectAllCourts()方法的返回值
        List<Court> courts = courtMapper.selectAllCourts();

// 使用for-each循环遍历courts列表
        for (Court court : courts) {
            // 输出每个球场的详细信息
            System.out.println("球场ID: " + court.getCourtId());
            System.out.println("位置: " + court.getLocation());
            System.out.println("场地号码: " + court.getCourtNumber());
            System.out.println("分类名称: " + court.getCategoryName());
            System.out.println("-----------------------------");
        }

    }
}