package cn.sduonline.sdu_lantern_festival_2021.dao;

import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.Room;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface RoomMapper {
    @Insert("INSERT INTO room (room_id, room_capacity, user1_id, user2_id, room_end_state, room_end_reason)" +
            "VALUES (#{roomID}, #{roomCapacity}, #{user1ID}, #{user2ID}, #{roomEndState}, #{roomEndReason})")
    void insertRoom(Room room);
}
