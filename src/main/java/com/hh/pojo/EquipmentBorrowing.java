package com.hh.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EquipmentBorrowing {
        private Integer borrowingId;
        private Integer equipmentId;
        private String coverImg;
        private String EquipmentName;
        private Integer userId;
        private String username;//用户名
        private String location;
        private Integer borrowQuantity;
        private LocalDateTime borrowTime;
        private LocalDateTime returnTime;
        private Integer borrowStatus;

}
