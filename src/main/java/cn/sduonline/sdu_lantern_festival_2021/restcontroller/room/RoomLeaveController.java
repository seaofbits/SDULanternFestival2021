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

import java.util.Map;


@Controller
@ResponseBody
public class RoomLeaveController {
    @Autowired
    RedisUtil redisUtil;

    @Auth
    @PostMapping(path = "/room/leave")
    public Response join(@RequestParam("room_id") int roomID,
                         @RequestAttribute("user_id") long userID) {
        // 异常情况
        if (!redisUtil.hasKey("room-" + roomID)) {
            return Response.fail(ResponseCode.ROOM_INVALID_ID);
        }

        Map attr = redisUtil.hmget("room-" + roomID);
        int whichUser;
        if (attr.get("user1_id").equals(userID)) {
            whichUser = 1;
        } else if (attr.get("user2_id").equals(userID)) {
            whichUser = 2;
        } else {
            return Response.fail(ResponseCode.ROOM_INVALID_USER);
        }
        if (attr.get("state").equals("terminated")) {
            return Response.fail(ResponseCode.ROOM_INVALID_STATE,
                    Map.of("now_state", attr.get("state")));
        }

        // 用户离开房间
        redisUtil.hset("room-" + roomID, "state", "terminated");
        redisUtil.hset("room-" + roomID, "end_state", "terminated");
        redisUtil.hset("room-" + roomID, "end_reason", "用户" + whichUser + "主动离开房间。");
        return Response.success();
    }
}
