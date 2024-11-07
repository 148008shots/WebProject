package com.hh.service;

import com.hh.pojo.Categories;

import java.util.List;

public interface CategoriesService {
    List<Categories> getAllCategories();

    int insertCategory(int categoryId, String name);

    int updateCategory(int categoryId, String name);

    int deleteCategory(int categoryId);
}
