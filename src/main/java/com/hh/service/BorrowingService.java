package com.hh.service;

import com.hh.pojo.EquipmentBorrowing;

import java.util.List;

public interface BorrowingService {
    List<EquipmentBorrowing> getBorrowByUserId(Integer id);
}
