package com.hh.mapper;

import com.hh.pojo.Categories;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoriesMapper {
    @Select("SELECT * FROM categories")
    List<Categories> selectAllCategories();
    @Insert("INSERT INTO categories (category_id, name) VALUES (#{categoryId}, #{name})")
    int insertCategory(@Param("categoryId") int categoryId, @Param("name") String name);

    @Update("UPDATE categories SET name = #{name} WHERE category_id = #{categoryId}")
    int updateCategory(@Param("categoryId") int categoryId, @Param("name") String name);

    @Delete("DELETE FROM categories WHERE category_id = #{categoryId}")
    int deleteCategory(@Param("categoryId") int categoryId);
}
