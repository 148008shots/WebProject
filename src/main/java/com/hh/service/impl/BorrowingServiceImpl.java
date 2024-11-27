package com.hh.service.impl;

import com.hh.mapper.BorrowingMapper;
import com.hh.pojo.EquipmentBorrowing;
import com.hh.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowingServiceImpl implements BorrowingService {
    @Autowired
    private BorrowingMapper borrowingMapper;

    @Override
    public List<EquipmentBorrowing> getBorrowByUserId(Integer id) {
        return borrowingMapper.findAppointmentsByDate(id);
    }
}
