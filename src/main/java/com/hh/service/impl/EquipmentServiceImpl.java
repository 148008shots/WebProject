package com.hh.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hh.mapper.EquipmentMapper;
import com.hh.pojo.Equipment;
import com.hh.pojo.EquipmentBorrowing;
import com.hh.pojo.PageBean;
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
    public PageBean<Equipment> equipmentList(Integer pageNum, Integer pageSize, String searchEquipmentName) {
        PageBean<Equipment> pb= new PageBean<>();
        PageHelper.startPage(pageNum,pageSize);
        List<Equipment> eq = equipmentMapper.selectEquipment(searchEquipmentName);

        Page<Equipment> p= (Page<Equipment>) eq;

        pb.setItems(p.getResult());
        pb.setTotal(p.getTotal());
        return pb;
    }
}
