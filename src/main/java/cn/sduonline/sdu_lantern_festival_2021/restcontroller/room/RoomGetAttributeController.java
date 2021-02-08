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

import java.util.Iterator;
import java.util.Map;

@Controller
@ResponseBody
public class RoomGetAttributeController {
    @Autowired
    RedisUtil redisUtil;

    @Auth
    @PostMapping(path = "/room/get_attribute")
    public Response getAttr(@RequestParam("room_id") int roomID,
                            @RequestAttribute("user_id") long userID) {
        Map attr = redisUtil.hmget("room-" + roomID);
        // 异常情况
        if (attr == null) {
            return Response.fail(ResponseCode.ROOM_INVALID_ID);
        }
        if (!attr.get("user1_id").equals(userID)
                && !attr.get("user2_id").equals(userID)) {
            return Response.fail(ResponseCode.ROOM_INVALID_USER);
        }

        // 去掉所有private_开头的字段
        Iterator<String> iterator = attr.keySet().iterator();
        while(iterator.hasNext()){
            String str = iterator.next();
            if (str.startsWith("private_"))
                iterator.remove();
        }
        return Response.success(attr);
    }
}
