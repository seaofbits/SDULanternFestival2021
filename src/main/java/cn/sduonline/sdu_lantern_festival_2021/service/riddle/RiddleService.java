package cn.sduonline.sdu_lantern_festival_2021.service.riddle;

import cn.sduonline.sdu_lantern_festival_2021.dao.RiddleMapper;
import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Riddle;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiddleService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RiddleMapper riddleMapper;

    @Value("${riddle.total-number}")
    Integer totalNumber;

    public Riddle getRiddleByID(int riddleID) {
        return (Riddle) redisUtil.get("riddle-"+riddleID);
    }

    /**
     * 把所有谜语题目加载到redis中缓存起来
     */
    public void loadAllRiddlesToRedis() {
        List<Riddle> riddleList = riddleMapper.getAllRiddles();
        for (int i = 0; i < totalNumber; i++) {
            redisUtil.set("riddle-"+(i + 1),riddleList.get(i));
        }
    }
}
