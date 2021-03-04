package cn.sduonline.sdu_lantern_festival_2021.dao;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Ranking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RankingMapper {
    @Select("SELECT user_id, SUM(score) AS total_score, SUM(correct_num) AS total_correct_num, SUM(answered_num) AS total_answer_num " +
            "FROM answer_sheet GROUP BY user_id ORDER BY total_score DESC")
    List<Ranking> getRankingList();

    @Select("SELECT user_id, SUM(score) AS total_score, SUM(correct_num) AS total_correct_num, SUM(answered_num) AS total_answer_num " +
            "FROM answer_sheet GROUP BY user_id HAVING user_id = #{userID}")
    Ranking getRankingByID(@Param("userID") long userID);
}
