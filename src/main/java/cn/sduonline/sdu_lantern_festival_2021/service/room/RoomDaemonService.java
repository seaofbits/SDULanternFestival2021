package cn.sduonline.sdu_lantern_festival_2021.service.room;

import cn.sduonline.sdu_lantern_festival_2021.dao.RoomMapper;
import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Room;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class RoomDaemonService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RoomMapper roomMapper;

    Map<Integer, RoomActiveRecord> roomActiveRecords = new HashMap<>();

    /**
     * 定时把不活跃的房间的state标记为"terminated"，认为该房间已经超时关闭了。
     */
    @Scheduled(fixedRateString = "#{70 * 1000}")
    void roomTerminator() {
        Set<String> rooms = redisUtil.getRedisTemplate().keys("room-*");
        for (String key : rooms) {
            Map attr = redisUtil.hmget(key);
            // 房间已经结束了
            String state = (String) attr.get("state");
            if (state.equals("terminated")) {
                continue;
            }

            // 看是否要结束该房间
            int roomID = (int) attr.get("room_id");
            RoomActiveRecord activeRecord = roomActiveRecords.get(roomID);
            if (activeRecord == null) {
                activeRecord = new RoomActiveRecord(0, Long.MAX_VALUE);
                roomActiveRecords.put(roomID, activeRecord);
            }

            if (activeRecord.oldHash == attr.hashCode()) {        // 房间的attr在这期间没有更改
                // 不活跃条件：playing答题超出60秒，unfull超出1000秒，其他超出300秒
                if (state.equals("unfull") && System.currentTimeMillis() - activeRecord.lastActiveTime >= 65 * 1000 ||
                        state.equals("playing") && System.currentTimeMillis() - activeRecord.lastActiveTime >= 1000 * 1000 ||
                        System.currentTimeMillis() - activeRecord.lastActiveTime >= 300 * 1000) {
                    redisUtil.hset(key, "end_state", attr.get("state"));
                    redisUtil.hset(key, "end_reason", "超时结束");

                    redisUtil.hset(key, "state", "terminated");
                    roomActiveRecords.remove(roomID);
                    continue;
                }
            } else {
                // activeRecord.oldHash != attr.hashCode()，房间的attr在这期间有更改
                activeRecord.oldHash = attr.hashCode();
                activeRecord.lastActiveTime = System.currentTimeMillis();
            }
        }
    }


    /**
     * 定期把terminated的room的记录从redis删除，并把关键数据持久化至MySQL中
     */
    @Scheduled(fixedRateString = "#{100 * 1000}")
    void terminatedRoom() {
        Set<String> rooms = redisUtil.getRedisTemplate().keys("room-*");
        for (String key : rooms) {
            Map attr = redisUtil.hmget(key);

            String state = (String) attr.get("state");
            if (!state.equals("terminated")) {
                // 房间还没有结束，没法收拾残局
                continue;
            }

            // 把关键数据持久化
            Room room = new Room();
            room.setRoomID((int) attr.get("room_id"));
            room.setRoomCapacity((int) attr.get("capacity"));
            room.setUser1ID((long) attr.get("user1_id"));
            room.setUser2ID((long) attr.get("user2_id"));
            if (!attr.containsKey("end_reason")) {
                room.setRoomEndState("terminated");
                room.setRoomEndReason("正常结束");
            } else {
                room.setRoomEndState((String) attr.get("end_state"));
                room.setRoomEndReason((String) attr.get("end_reason"));
            }
            roomMapper.insertRoom(room);

            redisUtil.getRedisTemplate().delete(key);
        }
    }
}


class RoomActiveRecord {
    int oldHash;
    long lastActiveTime;

    public RoomActiveRecord() {
    }

    public RoomActiveRecord(int oldHash, long lastActiveTime) {
        this.oldHash = oldHash;
        this.lastActiveTime = lastActiveTime;
    }
}
