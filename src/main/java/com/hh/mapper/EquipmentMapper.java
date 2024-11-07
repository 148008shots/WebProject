package com.hh.mapper;

import com.hh.pojo.Equipment;
import com.hh.pojo.EquipmentBorrowing;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EquipmentMapper {
    @Select("select * from equipment")
    List<Equipment> getList();

    @Insert("INSERT INTO `equipment` (`name`, `equipment_count`, `location`, `cover_img`) " +
            "VALUES (#{name}, #{equipmentCount}, #{location}, #{coverImg})")
    int insertEquipment(Equipment equipment);

    @Delete("DELETE FROM `equipment` WHERE `equipment_id` = #{equipmentId}")
    int deleteEquipmentById(int equipmentId);

    @Update("UPDATE `equipment` " +
            "SET `name` = #{name}, `equipment_count` = #{equipmentCount}, `location` = #{location}, `cover_img` = #{coverImg} " +
            "WHERE `equipment_id` = #{equipmentId}")
    int updateEquipment(Equipment equipment);

    // 增加借出记录
    @Insert("INSERT INTO `equipmentborrowings` (`equipment_id`, `user_id`, `borrow_time`, `return_time`) " +
            "VALUES (#{equipmentId}, #{userId}, #{borrowTime}, #{returnTime})")
    int addBorrowing(EquipmentBorrowing borrowing);

    // 根据ID删除借出记录
    @Delete("DELETE FROM `equipmentborrowings` WHERE `borrowing_id` = #{borrowingId}")
    int deleteBorrowingById(int borrowingId);

    // 更新借出记录
    @Update("UPDATE `equipmentborrowings` SET `equipment_id` = #{equipmentId}, `user_id` = #{userId}, " +
            "`borrow_time` = #{borrowTime}, `return_time` = #{returnTime} WHERE `borrowing_id` = #{borrowingId}")
    int updateBorrowing(EquipmentBorrowing borrowing);
    // 查询全部预定信息
    @Select("SELECT * FROM `equipmentborrowings`")
    List<EquipmentBorrowing> getAllBorrowings();
}
