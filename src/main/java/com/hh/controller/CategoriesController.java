package com.hh.controller;

import com.hh.pojo.Categories;
import com.hh.pojo.Result;
import com.hh.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;
    @GetMapping
    public Result getAllCategories() {
        List<Categories> categories = categoriesService.getAllCategories();
        return Result.success(categories);
    }
    @PostMapping
    public Result<String> addCategory(@RequestParam int categoryId, @RequestParam String name) {
        int result = categoriesService.insertCategory(categoryId, name);
        if (result > 0) {
            return Result.success("类别添加成功");
        } else {
            return Result.error("类别添加失败");
        }
    }

    @PutMapping
    public Result<String> updateCategory(@RequestParam int categoryId, @RequestParam String name) {
        int result = categoriesService.updateCategory(categoryId, name);
        if (result > 0) {
            return Result.success("类别更新成功");
        } else {
            return Result.error("类别更新失败");
        }
    }

    @DeleteMapping
    public Result<String> deleteCategory(@RequestParam int categoryId) {
        int result = categoriesService.deleteCategory(categoryId);
        if (result > 0) {
            return Result.success("类别删除成功");
        } else {
            return Result.error("类别删除失败");
        }
    }

}
