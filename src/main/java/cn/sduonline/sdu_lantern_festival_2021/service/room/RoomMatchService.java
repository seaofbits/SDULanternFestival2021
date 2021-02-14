package cn.sduonline.sdu_lantern_festival_2021.service.room;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Ranking;
import cn.sduonline.sdu_lantern_festival_2021.service.info.RankingService;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomMatchService {
    // 所有正在等待匹配的用户
    public static final Set<Long> waitingUser = new HashSet<>();
    // 所有等待匹配的用户的（用户-胜率）值
    public static final ArrayList<UserWithWinningRate> matchPool = new ArrayList<>();


    @Autowired
    RankingService rankingService;

    @Autowired
    RedisUtil redisUtil;


    public void addMatchUser(long userID) {
        // 获取user的战斗值
        Ranking ranking = rankingService.getCachedRankingByID(userID);
        double winningRate;
        if (ranking == null) {
            winningRate = 0.5;
        } else {
            winningRate = (double) ranking.getTotalCorrectNum() / ranking.getTotalAnswerNum();
        }

        // 线程安全地加入待匹配集合中
        synchronized (matchPool) {
            if (waitingUser.contains(userID))
                return;
            waitingUser.add(userID);
            matchPool.add(new UserWithWinningRate(userID, winningRate));
        }
    }

    // 为所有的待匹配用户进行匹配，每10s会被调用一次（以yml配置文件的值为准）
    @Scheduled(fixedRateString = "${match-interface.match-interval}")
    public void matchAllUsers() {
        // 这个synchronized稍微有一点点重量……
        synchronized (matchPool) {
            // 无需匹配
            if (matchPool.isEmpty())
                return;

            // 无法匹配，直接忽略，客户端可以重新发起匹配请求。
            // 把未匹配成功的请求记录遗留下来反而会造成更大的混乱。
            if (matchPool.size() == 1) {
                matchPool.clear();
                waitingUser.clear();
            }

            // 把待匹配的用户提取成数组
            UserWithWinningRate[] matchArray = matchPool.toArray(new UserWithWinningRate[0]);
            int arrLength = matchArray.length;

            // 排序
            Arrays.sort(matchArray, Comparator.comparingDouble(o -> o.winningRate));
            // 洗牌算法
            if (arrLength < 10) {   // 数组太小，全部乱序洗牌就行
                shuffle(matchArray, 0, arrLength);
            } else {
                int percent30 = (int) (arrLength * 0.3);
                // 前30%洗牌一次
                shuffle(matchArray, 0, percent30);
                // 后30%洗牌一次
                shuffle(matchArray, arrLength - 1 - percent30, percent30);
                // 中间50%洗牌一次
                shuffle(matchArray, arrLength / 4, arrLength / 2);
            }

            // 遍历数组，每相邻的2个人匹配成同一个房间（所以要i+1也不越界才可以）
            for (int i = 0; (i + 1) < arrLength; i += 2) {
                int roomID = (int) redisUtil.incr("room_id_incr", 1);

                // 为两个玩家创建一个房间
                Map<String, Object> attributes = new HashMap<>();
                attributes.put("room_id", roomID);
                attributes.put("capacity", 2);
                attributes.put("user1_id", matchArray[i].userID);
                attributes.put("user2_id", matchArray[i + 1].userID);
                attributes.put("state", "full");
                redisUtil.hmset("room-" + roomID, attributes);

                // 匹配结果20秒内可访问有效
                redisUtil.set("match_result-" + matchArray[i].userID, roomID, 20);
                redisUtil.set("match_result-" + matchArray[i + 1].userID, roomID, 20);
            }

            // 可能剩下1个用户没有被匹配到，那也不理他了，理由见本方法差不多开头的地方
            matchPool.clear();
            waitingUser.clear();
        }
    }


    private static final Random random = new Random();

    /**
     * 洗牌算法
     *
     * @param arr        数组
     * @param startIndex 需要洗牌的区间左边开始的下标，包含。不检查参数是否合法
     * @param length     需要洗牌的区间长度，不检查参数是否合法
     * @param <T>        数组元素的类型
     */
    <T> void shuffle(T[] arr, int startIndex, int length) {
        T temp;
        int swapIndex;
        for (int i = 0; i < length; i++) {
            // 在[i,length]中选一个随机数
            swapIndex = random.nextInt(length - i) + i;
            // 所有脚标都加上offset
            temp = arr[i + startIndex];
            arr[i + startIndex] = arr[swapIndex + startIndex];
            arr[swapIndex + startIndex] = temp;
        }
    }
}

class UserWithWinningRate {
    long userID;
    double winningRate;

    public UserWithWinningRate(long userID, double winningRate) {
        this.userID = userID;
        this.winningRate = winningRate;
    }
}
