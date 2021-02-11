package cn.sduonline.sdu_lantern_festival_2021.dao;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Ranking;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RankingMapper {
    @Select("SELECT * FROM ranking_list ORDER BY total_points DESC LIMIT offset, limit")
    List<Ranking> getHighRankingList(@Param("offset") int offset, @Param("limit") int limit);

    @Select("SELECT * FROM ranking_list WHERE user_id=#{userID}")
    Ranking getRankingListByID(@Param("userID") long userID);

    @Insert("INSERT INTO riddle_list (user_id, total_points, total_correct_num, total_answer_num) " +
            "VALUES (#{userID}, #{newPoints}, #{newCorrectNum}, #{newAnswerNum})" +
            "ON DUPLICATE KEY UPDATE " +
            "total_points      = total_points      + #{newPoints}," +
            "total_correct_num = total_correct_num + #{newCorrectNum}, " +
            "total_answer_num  = total_answer_num  + #{newAnswerNum}")
    void addPointToRankingList(@Param("userID") long userID,
                               @Param("newPoints") int newPoints,
                               @Param("newCorrectNum") int newCorrectNum,
                               @Param("newAnswerNum") int newAnswerNum);
}
