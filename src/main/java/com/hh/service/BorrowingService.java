package com.hh.service;

import com.hh.pojo.Equipment;
import com.hh.pojo.EquipmentBorrowing;
import com.hh.pojo.PageBean;

import java.util.List;

public interface BorrowingService {
    List<EquipmentBorrowing> getBorrowByUserId(Integer id);

    boolean addEquipmentBorrowing(EquipmentBorrowing borrowing);

    boolean deleteEquipmentBorrowingById(int borrowingId);

    boolean updateEquipmentBorrowing(EquipmentBorrowing borrowing);

    boolean updateBorrowingStatus(Integer borrowingId, Integer newStatus);


    boolean increaseEquipmentQuantity(Integer equipmentId, Integer borrowQuantity);

    boolean decreaseEquipmentQuantity(Integer equipmentId, Integer borrowQuantity);

    List<EquipmentBorrowing> getAllEquipmentBorrowings();

    List<EquipmentBorrowing> findBorrowingDetailsByUserId(Integer id);
}
