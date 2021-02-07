package cn.sduonline.sdu_lantern_festival_2021.dao;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.RiddleList;
import org.apache.ibatis.annotations.*;

@Mapper
public interface RiddleListMapper {
    @Insert("INSERT INTO riddle_list (room_id, riddle1_id, riddle2_id, riddle3_id, riddle4_id, riddle5_id, riddle6_id) "
            + "VALUES (#{roomID}, #{riddle1ID}, #{riddle2ID}, #{riddle3ID}, #{riddle4ID}, #{riddle5ID}, #{riddle6ID})")
    @Options(useGeneratedKeys = true, keyProperty = "riddleListID")
    void insertRiddleList(RiddleList riddleList);

    @Select("SELECT * FROM riddle_list WHERE riddle_list_id=#{riddleListID}")
    RiddleList getRiddleListByID(@Param("riddleListID") int riddleListID);

}
