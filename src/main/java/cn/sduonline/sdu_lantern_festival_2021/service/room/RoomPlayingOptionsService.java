package cn.sduonline.sdu_lantern_festival_2021.service.room;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Riddle;
import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.RiddleList;
import cn.sduonline.sdu_lantern_festival_2021.service.riddle.RiddleListService;
import cn.sduonline.sdu_lantern_festival_2021.service.riddle.RiddleService;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * 房间游戏的各种操作
 */
@Service
public class RoomPlayingOptionsService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RiddleService riddleService;

    @Autowired
    RiddleListService riddleListService;

    @Async
    public void startPlaying(int roomID) {
        String nowState = (String) redisUtil.hget("room-" + roomID, "state");
        if (!nowState.equals("full") && !nowState.equals("end")) {
            // 注意这个Exception最终会被throw回到线程池，因为这是异步方法，用了一个新的线程来执行其中的代码
            throw new IllegalStateException("Illegal Room State");
        }
        // 先缓冲1.5s
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 初始化房间的题目
        generateRiddleListFor(roomID);
        // 初始化二人的答题区域
        redisUtil.hset("room-" + roomID, "user1_answers", new ArrayList<String>());
        redisUtil.hset("room-" + roomID, "user2_answers", new ArrayList<String>());

        // 初始化二人答对题目数量
        redisUtil.hset("room-" + roomID, "user1_score", 0);
        redisUtil.hset("room-" + roomID, "user2_score", 0);

        // 初始化现在正在答第几题
        redisUtil.hset("room-" + roomID, "now_riddle_num", 1);

        // 初始化正确答案数组
        redisUtil.hset("room-" + roomID, "riddle_answers", new ArrayList<String>());

        // 最后才真正把房间的state变成playing
        redisUtil.hset("room-" + roomID, "state", "playing");
    }


    public void answer(int roomID, int num, int whichUser, String answer) {
        // 检查是否能answer
        ArrayList<String> user1Answers = (ArrayList<String>) redisUtil.hget("room-" + roomID, "user1_answers");
        ArrayList<String> user2Answers = (ArrayList<String>) redisUtil.hget("room-" + roomID, "user2_answers");

    }


    void generateRiddleListFor(int roomID) {
        // 该方法会在MySQL内创建这套题的记录
        RiddleList riddleList = riddleListService.generateNewRiddleList(roomID);

        // 把各个题目的题面放到数组里，加入room attribute里
        int[] riddleIDs = riddleList.getRiddleIDsAsArray();
        // 公开：题面
        ArrayList<String> riddleContents = new ArrayList<>();
        // 私有：所有信息
        ArrayList<Riddle> privateRiddles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Riddle riddle = riddleService.getRiddleByID(riddleIDs[i]);
            // 抹去答案信息，存在公开题面那里
            riddleContents.add(riddle.getContent() + "（" + riddle.getTip() + "）");
            privateRiddles.add(riddle);
        }
        // 公开题目套题id，题目内容数组
        redisUtil.hset("room-" + roomID, "riddle_list_id", riddleList.getRiddleListID());
        redisUtil.hset("room-" + roomID, "riddle_contents", riddleContents);
        // 把含答案的题目信息保存为private字段
        redisUtil.hset("room-" + roomID, "private_riddles", privateRiddles);
    }
}
