package com.hh.mapper;

import com.hh.pojo.Img;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImgMapper {
    @Insert("insert into imgs(picurl,picname) value(#{pictureName},#{pictureUrl})")
    void save(Img img);
}
