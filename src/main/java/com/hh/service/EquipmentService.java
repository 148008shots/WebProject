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

    PageBean<Equipment> equipmentList(Integer pageNum, Integer pageSize, String searchEquipmentName);
}



