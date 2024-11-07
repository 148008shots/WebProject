package com.hh.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EquipmentBorrowing {
        private Integer borrowingId;
        private Integer equipmentId;
        private Integer userId;
        private LocalDateTime borrowTime;
        private LocalDateTime returnTime;
}
