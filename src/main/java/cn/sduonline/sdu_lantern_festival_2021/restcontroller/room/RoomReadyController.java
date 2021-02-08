package cn.sduonline.sdu_lantern_festival_2021.restcontroller.room;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector.Auth;
import cn.sduonline.sdu_lantern_festival_2021.service.pk.RoomPlayingOptionsService;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Map;

@Controller
@ResponseBody
public class RoomReadyController {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RoomPlayingOptionsService roomPlayingOptionsService;

    @Auth
    @PostMapping(path = "/room/ready")
    public Response ready(@RequestParam("room_id") int roomID,
                          @RequestAttribute("user_id") long userID) {
        Map attr = redisUtil.hmget("room-" + roomID);
        // 异常情况
        if (attr == null) {
            return Response.fail(ResponseCode.ROOM_INVALID_ID);
        }
        if (!attr.get("state").equals("full")
                && !attr.get("state").equals("end")) {
            return Response.fail(ResponseCode.ROOM_INVALID_STATE,
                    Map.of("now_state", attr.get("state")));
        }
        if (!attr.get("user1_id").equals(userID)
                && !attr.get("user2_id").equals(userID)) {
            return Response.fail(ResponseCode.ROOM_INVALID_USER);
        }

        // 获取ready_users属性
        ArrayList<Long> readyUsers = (ArrayList<Long>) attr.get("ready_users");
        if (readyUsers == null) {
            readyUsers = new ArrayList<>();
        }
        readyUsers.add(userID);
        attr.put("ready_users", readyUsers);

        // 如果房间满员了，就准备开始答题了
        if (attr.get("capacity").equals(readyUsers.size())) {
            // 异步方法
            roomPlayingOptionsService.startPlaying(roomID);
        }
        return Response.success();
    }
}
