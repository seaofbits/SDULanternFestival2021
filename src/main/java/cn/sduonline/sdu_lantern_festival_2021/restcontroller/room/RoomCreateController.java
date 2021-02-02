package cn.sduonline.sdu_lantern_festival_2021.restcontroller.room;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@ResponseBody
public class RoomCreateController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Auth
    @PostMapping(path = "/room/create")
    public Response create(@RequestParam("capacity") int capacity,
                           @RequestAttribute("user_id") long userID) {
        if (capacity != 1 && capacity != 2)
            return Response.fail(ResponseCode.ERROR_PARAMETER_INVALID_VALUE);

        // 创建一个房间（房间id自增）
        long room_id = stringRedisTemplate.opsForValue().increment("room_id");

        String key = "room-" + room_id;
        stringRedisTemplate.opsForHash().put(key, "room_id", String.valueOf(room_id));
        stringRedisTemplate.opsForHash().put(key, "capacity", String.valueOf(capacity));
        stringRedisTemplate.opsForHash().put(key, "user1_id", String.valueOf(userID));
        stringRedisTemplate.opsForHash().put(key, "user2_id", String.valueOf(-1));
        String state;
        if (capacity == 1) {
            state = "full";
        } else {
            state = "unfull";
        }
        stringRedisTemplate.opsForHash().put(key, "state", state);

        return Response.success(Map.of("room_id", room_id));
    }
}
