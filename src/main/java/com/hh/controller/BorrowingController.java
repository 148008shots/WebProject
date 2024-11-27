package com.hh.controller;

import com.hh.pojo.EquipmentBorrowing;
import com.hh.pojo.Result;
import com.hh.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}