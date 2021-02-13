package cn.sduonline.sdu_lantern_festival_2021.restcontroller.room;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector.Auth;
import cn.sduonline.sdu_lantern_festival_2021.service.room.RoomPlayingOptionsService;
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
public class RoomAnswerController {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RoomPlayingOptionsService playingService;

    @Auth
    @PostMapping(path = "/room/answer")
    public Response getAttr(@RequestParam("room_id") int roomID,
                            @RequestParam("num") int num,
                            @RequestParam("ans") String ans,
                            @RequestAttribute("user_id") long userID) {
        // 检验参数
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

        if (!attr.get("state").equals("playing")) {
            return Response.fail(ResponseCode.ROOM_INVALID_STATE,
                    Map.of("now_state", attr.get("state")));
        }

        String errMsg = playingService.answer(roomID, num, whichUser, ans);
        if (errMsg.equals("correct")) {
            return Response.success(Map.of("is_correct", true));
        } else if (errMsg.equals("not_correct")) {
            return Response.success(Map.of("is_correct", false));
        } else if (errMsg.equals("error_num")) {
            return Response.fail(ResponseCode.ROOM_INVALID_ANSWER_NUM);
        } else {
            return Response.fail(ResponseCode.ERROR_UNKNOWN, errMsg);
        }
    }
}

