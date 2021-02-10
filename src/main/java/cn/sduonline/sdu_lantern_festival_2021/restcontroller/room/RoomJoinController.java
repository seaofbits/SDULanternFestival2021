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
public class RoomJoinController {
    @Autowired
    RedisUtil redisUtil;

    @Auth
    @PostMapping(path = "/room/join")
    public Response join(@RequestParam("room_id") int roomID,
                         @RequestAttribute("user_id") long userID) {
        // 异常情况
        if (!redisUtil.hasKey("room-" + roomID)) {
            return Response.fail(ResponseCode.ROOM_INVALID_ID);
        }

        Map attr = redisUtil.hmget("room-" + roomID);
        if (!attr.get("state").equals("unfull")) {
            return Response.fail(ResponseCode.ROOM_INVALID_STATE,
                    Map.of("now_state", attr.get("state")));
        }
        if (attr.get("user1_id").equals(userID)) {
            return Response.fail(ResponseCode.ROOM_USER_ALREADY_IN);
        }

        // 把用户id加入该房间
        attr.put("user2_id", userID);
        // 因为join，房间状态变成了full
        attr.put("state", "full");
        redisUtil.hmset("room-" + roomID, attr);
        return Response.success();
    }
}
