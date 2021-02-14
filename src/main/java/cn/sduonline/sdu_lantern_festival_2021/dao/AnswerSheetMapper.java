package cn.sduonline.sdu_lantern_festival_2021.dao;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.AnswerSheet;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface AnswerSheetMapper {
    @Insert("INSERT INTO answer_sheet (riddle_list_id, user_id, answer1, answer2, answer3, answer4, answer5, answer6, answered_num, correct_num, multiple, score)" +
            "VALUES (#{riddleListID}, #{userID}, #{answer1}, #{answer2}, #{answer3}, #{answer4}, #{answer5}, #{answer6}, #{answeredNum}, #{correctNum}, #{multiple}, #{score})")
    @Options(useGeneratedKeys = true, keyProperty = "answerSheetID")
    void insert(AnswerSheet answerSheet);
}
