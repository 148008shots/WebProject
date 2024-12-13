package com.hh.controller;

import com.hh.pojo.*;
import com.hh.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/getAllEquipments")
    public Result getAllEquipments(){
        List<Equipment> eq =  equipmentService.getEquipmentList();
        return Result.success(eq);
    }

    @GetMapping
    public Result<PageBean<Equipment>>  listEquipment(
            Integer pageNum,
            Integer pageSize,
            @RequestParam(required = false) String searchEquipmentName // 场地名称
    ) {
        // 调用服务层方法获取球场信息列表
        PageBean<Equipment> pageBean= equipmentService.equipmentList(pageNum,pageSize,searchEquipmentName);
        return Result.success(pageBean);
    }
    @PostMapping("/addEquipment")
    public Result addEquipment(@RequestBody Equipment equipment) {
        boolean result = equipmentService.addEquipment(equipment);
        return result ? Result.success("添加成功") : Result.error("添加失败");
    }
    @DeleteMapping("/deleteEquipment/{equipmentId}")
    public Result deleteEquipment(@PathVariable int equipmentId) {
        boolean result = equipmentService.deleteEquipmentById(equipmentId);
        return result ? Result.success("删除成功") : Result.error("删除失败");
    }
    @PutMapping("/updateEquipment")
    public Result updateEquipment(@RequestBody Equipment equipment) {
        System.out.println(equipment);
        boolean result = equipmentService.updateEquipment(equipment);
        return result ? Result.success("更新成功") : Result.error("更新失败");
    }
}
