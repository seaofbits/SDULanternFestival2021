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
import java.util.Map;

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
        redisUtil.hset("room-" + roomID, "user1_correct_num", 0);
        redisUtil.hset("room-" + roomID, "user2_correct_num", 0);

        // 初始化现在正在答第几题（从1开始）
        redisUtil.hset("room-" + roomID, "now_riddle_num", 1);

        // 初始化（公布的）正确答案数组
        redisUtil.hset("room-" + roomID, "correct_answers", new ArrayList<String>());

        // 最后才真正把房间的state变成playing
        redisUtil.hset("room-" + roomID, "state", "playing");
    }


    /**
     * 用户回答一个问题
     *
     * @param roomID    哪个房间号
     * @param num       回答第几题
     * @param whichUser 为1或者2，代表用户1或者用户2
     * @param answer    答案
     * @return "correct"或者“not_correct”代表答案正确与否，或者返回导致异常的原因
     */
    public synchronized String answer(int roomID, int num, int whichUser, String answer) {
        // 检查是否能answer
        int nowRiddleNum = (int) redisUtil.hget("room-" + roomID, "now_riddle_num");
        if (nowRiddleNum != num) {
            return "error_num";
        }

        Map attr = redisUtil.hmget("room-" + roomID);
        // 存好用户答案
        ArrayList<String> user1Answers = (ArrayList<String>) attr.get("user1_answers");
        ArrayList<String> user2Answers = (ArrayList<String>) attr.get("user2_answers");
        if (whichUser == 1) {
            user1Answers.add(answer);
            user2Answers.add(null);
        } else {      // whichUser == 2
            user2Answers.add(answer);
            user1Answers.add(null);
        }
        attr.put("user1_answers", user1Answers);
        attr.put("user2_answers", user2Answers);


        // 公布本题正确答案
        ArrayList<String> riddleAnswers = (ArrayList<String>) attr.get("correct_answers");
        ArrayList<String> allAnswers = (ArrayList<String>) attr.get("private_all_correct_answers");
        riddleAnswers.add(allAnswers.get(nowRiddleNum - 1));

        // 检查用户的答案是否正确，以备返回
        boolean isCorrect = answer.equals(riddleAnswers.get(nowRiddleNum - 1));
        if (isCorrect) {
            // 更新正确答案对的题目的数目
            int correctNum = (int) attr.get("user" + whichUser + "_correct_num");
            correctNum++;
            attr.put("user" + whichUser + "_correct_num", correctNum);
        }

        // 更新房间now_riddle_num和房间状态
        nowRiddleNum++;
        attr.put("now_riddle_num", nowRiddleNum);
        if (nowRiddleNum == 7) {
            attr.put("state", "end");
        }

        // 把更新的房间状态应用到redis上，然后返回
        redisUtil.hmset("room-" + roomID, attr);
        return isCorrect ? "correct" : "not_correct";
    }


    void generateRiddleListFor(int roomID) {
        // 该方法会在MySQL内创建这套题的记录
        RiddleList riddleList = riddleListService.generateNewRiddleList(roomID);

        // 把各个题目的题面放到数组里，加入room attribute里
        int[] riddleIDs = riddleList.getRiddleIDsAsArray();
        // 公开：题面
        ArrayList<String> riddleContents = new ArrayList<>();
        // 私有：所有信息
        ArrayList<String> correctAnswers = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Riddle riddle = riddleService.getRiddleByID(riddleIDs[i]);
            // 抹去答案信息，存在公开题面那里
            riddleContents.add(riddle.getContent() + "（" + riddle.getTip() + "）");
            correctAnswers.add(riddle.getAns());
        }
        // 公开题目套题id，题目内容数组
        redisUtil.hset("room-" + roomID, "riddle_list_id", riddleList.getRiddleListID());
        redisUtil.hset("room-" + roomID, "riddle_contents", riddleContents);
        // 把正确答案题目信息保存为private字段
        redisUtil.hset("room-" + roomID, "private_all_correct_answers", correctAnswers);
    }
}
