package com.hh.mapper;

import com.hh.pojo.Bookings;
import com.hh.pojo.EquipmentBorrowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BorrowingMapper {

    @Select("SELECT * FROM equipmentborrowings WHERE user_id=#{id}")
    List<EquipmentBorrowing> findAppointmentsByDate(Integer id);
}
