package cn.sduonline.sdu_lantern_festival_2021.dao;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user (user_id, nickname) " +
            "VALUES (#{userID}, #{nickname})" +
            "ON DUPLICATE KEY UPDATE nickname=#{nickname}")
    void setUserNickname(User user);

    @Select("SELECT * FROM user WHERE user_id=#{userID}")
    User getUserByID(@Param("userID") long userID);
}
