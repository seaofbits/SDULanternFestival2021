package cn.sduonline.sdu_lantern_festival_2021.service.info;

import cn.sduonline.sdu_lantern_festival_2021.dao.RankingMapper;
import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Ranking;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RankingMapper rankingMapper;

    /**
     * 每60秒刷新一次排行榜
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void refreshRankingCache() {
        redisUtil.set("ranking_cache", rankingMapper.getRankingList());
    }

    /**
     * 返回用户的（可能是缓存的）排名
     *
     * @param userID 用户id
     * @return 用户的排名
     */
    public Ranking getCachedRankingByID(long userID) {
        List<Ranking> rankingList = (List<Ranking>) (Object) redisUtil.get("ranking_cache");
        for (Ranking ranking : rankingList) {
            if (ranking.getUserID() == userID) {
                return ranking;
            }
        }
        return rankingMapper.getRankingByID(userID);
    }

    /**
     * 返回用户的（可能是缓存的）排名
     *
     * @param offset 排名偏移
     * @param limit  排名链接
     * @return 用户的排名
     */
    public List<Ranking> getCachedRankingList(int offset, int limit) {
        if (!redisUtil.hasKey("ranking_cache")) {
            refreshRankingCache();
        }
        List<Ranking> rankingList = (List<Ranking>) (Object) redisUtil.get("ranking_cache");
        if (offset < 0 || offset >= rankingList.size() || limit < 0) {
            // 参数有误，返回一个空列表
            return new ArrayList<>();
        } else if (offset + limit >= rankingList.size()) {
            // 返回全表
            return rankingList;
        } else {
            // 返回指定范围的排名列表
            return rankingList.subList(offset, offset + limit);
        }
    }
}
