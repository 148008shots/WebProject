package com.hh.service;

import com.hh.pojo.Equipment;
import com.hh.pojo.EquipmentBorrowing;
import com.hh.pojo.PageBean;

import java.util.List;

public interface EquipmentService {
    List<Equipment> getEquipmentList();

    boolean addEquipment(Equipment equipment);

    boolean deleteEquipmentById(int equipmentId);

    boolean updateEquipment(Equipment equipment);
    List<EquipmentBorrowing> getAllEquipmentBorrowings();
    boolean addEquipmentBorrowing(EquipmentBorrowing borrowing);
    boolean deleteEquipmentBorrowingById(int borrowingId);
    boolean updateEquipmentBorrowing(EquipmentBorrowing borrowing);

    boolean updateBorrowingStatus(Integer borrowingId, Integer newStatus);


    boolean increaseEquipmentQuantity(Integer equipmentId, Integer borrowQuantity);

    boolean decreaseEquipmentQuantity(Integer equipmentId, Integer borrowQuantity);

    PageBean<Equipment> equipmentList(Integer pageNum, Integer pageSize, String searchEquipmentName);
}



