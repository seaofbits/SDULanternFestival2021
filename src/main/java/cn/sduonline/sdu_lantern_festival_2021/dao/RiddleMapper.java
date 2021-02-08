package cn.sduonline.sdu_lantern_festival_2021.dao;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Riddle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RiddleMapper {
    @Select("SELECT * FROM riddle WHERE riddle_id=#{riddleID}")
    Riddle getRiddle(@Param("riddleID") int riddleID);

    @Select("SELECT * FROM riddle")
    List<Riddle> getAllRiddles();
}
