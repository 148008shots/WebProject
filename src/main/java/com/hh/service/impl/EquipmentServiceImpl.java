package com.hh.service.impl;

import com.hh.mapper.EquipmentMapper;
import com.hh.pojo.Equipment;
import com.hh.pojo.EquipmentBorrowing;
import com.hh.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public List<Equipment> getEquipmentList() {
        return equipmentMapper.getList();
    }

    @Override
    public boolean addEquipment(Equipment equipment) {
        if (equipmentMapper.insertEquipment(equipment) != 0){
            return  true;
        }
        return false;
    }

    @Override
    public boolean deleteEquipmentById(int equipmentId) {
        if (equipmentMapper.deleteEquipmentById(equipmentId) != 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateEquipment(Equipment equipment) {
        if (equipmentMapper.updateEquipment(equipment) != 0){
            return  true;
        }
        return false;
    }

    @Override
    public List<EquipmentBorrowing> getAllEquipmentBorrowings() {
        return equipmentMapper.findAllWithUserInfoAndEquipmentInfo();
    }

    @Override
    public boolean addEquipmentBorrowing(EquipmentBorrowing borrowing) {
        return equipmentMapper.addBorrowing(borrowing) > 0;
    }

    @Override
    public boolean deleteEquipmentBorrowingById(int borrowingId) {
        return equipmentMapper.deleteBorrowingById(borrowingId) > 0;
    }

    @Override
    public boolean updateEquipmentBorrowing(EquipmentBorrowing borrowing) {
        return equipmentMapper.updateBorrowing(borrowing) > 0;
    }

    @Override
    public boolean updateBorrowingStatus(Integer borrowingId, Integer newStatus) {

        int result = equipmentMapper.updateBorrowStatus(borrowingId, newStatus);
        return result > 0;
    }
}
