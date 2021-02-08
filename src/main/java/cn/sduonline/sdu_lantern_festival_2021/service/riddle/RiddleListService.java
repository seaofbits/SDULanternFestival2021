package cn.sduonline.sdu_lantern_festival_2021.service.riddle;

import cn.sduonline.sdu_lantern_festival_2021.dao.RiddleListMapper;
import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.RiddleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RiddleListService {
    @Autowired
    RiddleListMapper riddleListMapper;

    @Value("${riddle.total-number}")
    Integer totalNumber;

    Random random = new Random();

    /**
     * 生成一套新的房间问答题目，并在MySQL中保存
     * @param roomID 这套题目属于哪个房间？
     * @return 一套题目
     */
    public RiddleList generateNewRiddleList(int roomID) {
        int[] riddles = new int[6];
        for (int i = 0; i < 6; i++) {
            riddles[i] = random.nextInt(totalNumber) + 1;
            // 内循环检查riddles内是否有重复的数字
            for (int j = 0; j < i; j++) {
                if (riddles[i] == riddles[j]) {     // 如果发现重复
                    // 这个操作会抵消掉i++，重新生成原第i个数
                    i -= 1;
                    break;
                }
            }
        }

        RiddleList riddleList = new RiddleList();
        riddleList.setRoomID(roomID);
        riddleList.setRiddle1ID(riddles[0]);
        riddleList.setRiddle2ID(riddles[1]);
        riddleList.setRiddle3ID(riddles[2]);
        riddleList.setRiddle4ID(riddles[3]);
        riddleList.setRiddle5ID(riddles[4]);
        riddleList.setRiddle6ID(riddles[5]);
        riddleListMapper.insertRiddleList(riddleList);
        return riddleList;
    }
}
