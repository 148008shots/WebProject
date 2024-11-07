package com.hh.service.impl;

import com.hh.mapper.CategoriesMapper;
import com.hh.pojo.Categories;
import com.hh.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl implements CategoriesService {
    @Autowired
    private CategoriesMapper categoriesMapper;

    @Override
    public List<Categories> getAllCategories() {
        return categoriesMapper.selectAllCategories();
    }
    @Override
    public int insertCategory(int categoryId, String name) {
        return categoriesMapper.insertCategory(categoryId, name);
    }

    @Override
    public int updateCategory(int categoryId, String name) {
        return categoriesMapper.updateCategory(categoryId, name);
    }

    @Override
    public int deleteCategory(int categoryId) {
        return categoriesMapper.deleteCategory(categoryId);
    }
}
