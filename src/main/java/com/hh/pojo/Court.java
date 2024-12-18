package com.hh.pojo;

import lombok.Data;

@Data
public class Court {
    private Integer courtId;//场地ID
    private Integer categoryId;//场地分类ID
    private String location;//场地位置
    private String courtNumber;//场地号码
    private String coverImg;//场地封面
    private String categoryName;//分类名称
    private Integer price;//积分数
}
