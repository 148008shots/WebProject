package com.hh.controller;

import com.hh.pojo.Equipment;
import com.hh.pojo.EquipmentBorrowing;
import com.hh.pojo.Result;
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
        boolean result = equipmentService.updateEquipment(equipment);
        return result ? Result.success("更新成功") : Result.error("更新失败");
    }
    @GetMapping("/getAllBorrowings")
    public Result getAllBorrowings() {
        List<EquipmentBorrowing> borrowings = equipmentService.getAllEquipmentBorrowings();
        return Result.success(borrowings);
    }

    @PostMapping("/addBorrowing")
    public Result addBorrowing(@RequestBody EquipmentBorrowing borrowing) {
        boolean result = equipmentService.addEquipmentBorrowing(borrowing);
        return result ? Result.success("添加成功") : Result.error("添加失败");
    }

    @DeleteMapping("/deleteBorrowing/{borrowingId}")
    public Result deleteBorrowing(@PathVariable int borrowingId) {
        boolean result = equipmentService.deleteEquipmentBorrowingById(borrowingId);
        return result ? Result.success("删除成功") : Result.error("删除失败");
    }

    @PutMapping("/updateBorrowing")
    public Result updateBorrowing(@RequestBody EquipmentBorrowing borrowing) {
        boolean result = equipmentService.updateEquipmentBorrowing(borrowing);
        return result ? Result.success("更新成功") : Result.error("更新失败");
    }

}
