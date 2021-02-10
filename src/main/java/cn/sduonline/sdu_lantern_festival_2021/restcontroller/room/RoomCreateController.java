package cn.sduonline.sdu_lantern_festival_2021.restcontroller.room;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector.Auth;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
public class RoomCreateController {
    @Autowired
    RedisUtil redisUtil;

    @Auth
    @PostMapping(path = "/room/create")
    public Response create(@RequestParam("capacity") int capacity,
                           @RequestAttribute("user_id") long userID) {
        if (capacity != 1 && capacity != 2)
            return Response.fail(ResponseCode.ERROR_PARAMETER_INVALID_VALUE);

        // 创建一个房间（房间id自增）
        int roomID = (int) redisUtil.incr("room_id_incr", 1);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("room_id", roomID);
        attributes.put("capacity", capacity);
        attributes.put("user1_id", userID);
        attributes.put("user2_id", -1);
        if (capacity == 1) {
            attributes.put("state", "full");
        } else {
            attributes.put("state", "unfull");
        }
        redisUtil.hmset("room-" + roomID, attributes);

        return Response.success(Map.of("room_id", roomID));
    }
}
