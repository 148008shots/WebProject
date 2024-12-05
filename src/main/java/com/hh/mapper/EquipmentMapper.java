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
    @Insert("INSERT INTO `equipmentborrowings` (`equipment_id`, `user_id`, `borrow_time`, `return_time`, `borrow_quantity`) " +
            "VALUES (#{equipmentId}, #{userId}, #{borrowTime}, #{returnTime}, #{borrowQuantity})")
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
    //查询所用详细的借用信息结合用户表查询用户名和器材表查询器材图片
    @Select("SELECT " +
            "eb.borrowing_id AS borrowingId, " +
            "eb.equipment_id AS equipmentId, " +
            "e.cover_img AS coverImg, " +
            "e.name AS EquipmentName, " +
            "eb.user_id AS userId, " +
            "u.username AS username, " +
            "eb.borrow_quantity AS borrowQuantity, " +
            "eb.borrow_time AS borrowTime, " +
            "eb.return_time AS returnTime, " +
            "eb.borrow_status AS borrowStatus " +
            "FROM equipmentborrowings eb " +
            "JOIN user u ON eb.user_id = u.id " +
            "JOIN equipment e ON eb.equipment_id = e.equipment_id")
    List<EquipmentBorrowing> findAllWithUserInfoAndEquipmentInfo();

    @Update("UPDATE equipmentborrowings SET borrow_status = #{newStatus} WHERE borrowing_id = #{borrowingId}")
    int updateBorrowStatus(@Param("borrowingId") int borrowingId, @Param("newStatus") int newStatus);

    // 更新器材数量，changeQuantity为正数时增加，为负数时减少
    @Update("UPDATE equipment SET equipment_count = equipment_count + #{changeQuantity} WHERE equipment_id = #{equipmentId}")
    boolean updateEquipmentQuantity(@Param("equipmentId") int equipmentId, @Param("changeQuantity") int changeQuantity);

    List selectEquipment(String searchEquipmentName);
}

