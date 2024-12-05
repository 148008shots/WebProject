package com.hh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hh.mapper.BorrowingMapper;
import com.hh.pojo.Equipment;
import com.hh.pojo.EquipmentBorrowing;
import com.hh.pojo.PageBean;
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

    @Override
    public List<EquipmentBorrowing> getAllEquipmentBorrowings() {
        return borrowingMapper.findAllWithUserInfoAndEquipmentInfo();
    }

    @Override
    public boolean addEquipmentBorrowing(EquipmentBorrowing borrowing) {
        borrowing.setBorrowStatus(1);
        return borrowingMapper.addBorrowing(borrowing) > 0;
    }

    @Override
    public boolean deleteEquipmentBorrowingById(int borrowingId) {
        return borrowingMapper.deleteBorrowingById(borrowingId) > 0;
    }

    @Override
    public boolean updateEquipmentBorrowing(EquipmentBorrowing borrowing) {
        return borrowingMapper.updateBorrowing(borrowing) > 0;
    }

    @Override
    public boolean updateBorrowingStatus(Integer borrowingId, Integer newStatus) {

        int result = borrowingMapper.updateBorrowStatus(borrowingId, newStatus);
        return result > 0;
    }

    public boolean decreaseEquipmentQuantity(Integer equipmentId, Integer borrowQuantity) {
        // 减少器材数量的逻辑
        return borrowingMapper.updateEquipmentQuantity(equipmentId, -borrowQuantity);
    }

    public boolean increaseEquipmentQuantity(Integer equipmentId, Integer addQuantity) {
        // 增加器材数量的逻辑
        return borrowingMapper.updateEquipmentQuantity(equipmentId, addQuantity);
    }
}
