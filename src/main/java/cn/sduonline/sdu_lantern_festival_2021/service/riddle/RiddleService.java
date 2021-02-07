package cn.sduonline.sdu_lantern_festival_2021.service.riddle;

import cn.sduonline.sdu_lantern_festival_2021.dao.RiddleMapper;
import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Riddle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiddleService {
    @Autowired
    RiddleMapper riddleMapper;

    public String getRiddle(int riddleID) {
        Riddle riddle = riddleMapper.getRiddle(riddleID);
        return riddle.getContent() + "（" + riddle.getTip() + "）";
    }
}
