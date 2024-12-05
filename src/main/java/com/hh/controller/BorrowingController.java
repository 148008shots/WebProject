package com.hh.controller;

import com.hh.pojo.EquipmentBorrowing;
import com.hh.pojo.Result;
import com.hh.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Borrowing")
public class BorrowingController {

    private final BorrowingService borrowingService;

    @Autowired
    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @GetMapping("/getById/{id}")
    public Result<List<EquipmentBorrowing>> getBorrowById(@PathVariable Integer id) {
        List<EquipmentBorrowing> equipmentBorrowingList = borrowingService.getBorrowByUserId(id);
        return Result.success(equipmentBorrowingList);

    }

    @GetMapping("/getAllBorrowings")
    public Result getAllBorrowings() {
        List<EquipmentBorrowing> borrowings = borrowingService.getAllEquipmentBorrowings();
        return Result.success(borrowings);
    }

    @PostMapping("/addBorrowing")
    public Result addBorrowing(@RequestBody EquipmentBorrowing borrowing) {
        boolean result = borrowingService.addEquipmentBorrowing(borrowing);
        return result ? Result.success("添加成功") : Result.error("添加失败");
    }

    @DeleteMapping("/deleteBorrowing/{borrowingId}")
    public Result deleteBorrowing(@PathVariable int borrowingId) {
        boolean result = borrowingService.deleteEquipmentBorrowingById(borrowingId);
        return result ? Result.success("删除成功") : Result.error("删除失败");
    }

    @PutMapping("/updateBorrowing")
    public Result updateBorrowing(@RequestBody EquipmentBorrowing borrowing) {
        boolean result = borrowingService.updateEquipmentBorrowing(borrowing);
        return result ? Result.success("更新成功") : Result.error("更新失败");
    }

    @PutMapping("/updateBorrowing1")
    public Result updateBorrowingStatus(
            @RequestParam Integer borrowingId,
            @RequestParam Integer equipmentId,
            @RequestParam Integer newStatus,
            @RequestParam Integer borrowQuantity
    ) {
        boolean result;
//        预约更新预约表里面的状态
        borrowingService.updateBorrowingStatus(borrowingId, newStatus);
//        对器材表里面的器材数量进行修改
        if (newStatus == 1) {
            // 如果newStatus为1，执行减少数量操作
            result = borrowingService.decreaseEquipmentQuantity(equipmentId, borrowQuantity);
        } else if (newStatus == 2) {
            // 如果newStatus为2，执行增加数量操作
            result = borrowingService.increaseEquipmentQuantity(equipmentId, borrowQuantity);
        } else {
            // 如果newStatus不是1或2，返回错误
            return Result.error("状态码不正确，无法执行操作");
        }
        return result ? Result.success("操作成功") : Result.error("操作失败");
    }
}