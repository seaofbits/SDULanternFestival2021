package cn.sduonline.sdu_lantern_festival_2021.restcontroller.room;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector.Auth;
import cn.sduonline.sdu_lantern_festival_2021.service.room.RoomMatchService;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;


@Controller
@ResponseBody
public class RoomMatchController {
    @Autowired
    RoomMatchService roomMatchService;

    @Autowired
    RedisUtil redisUtil;


    @Value("#{new java.text.SimpleDateFormat('${match-interface.time-format}').parse('${match-interface.open-time}')}")
    Date openDate;

    @Value("#{new java.text.SimpleDateFormat('${match-interface.time-format}').parse('${match-interface.close-time}')}")
    Date closeDate;

    @Auth
    @PostMapping(path = "/room/match")
    public Response roomMatch(@RequestAttribute("user_id") long userID) {
        // 接口不在开放时间段内
        Date nowDate = new Date();
        if (nowDate.before(openDate) || nowDate.after(closeDate))
            return Response.fail(ResponseCode.ROOM_INVALID_CALL_TIME);

        // 如果有匹配结果，直接返回（匹配结果会维持30s，这里的作用是防止重复请求）
        if (redisUtil.hasKey("match_result-" + userID)) {
            int roomID = (int) redisUtil.get("match_result-" + userID);
            return Response.success(Map.of("room_id", roomID));
        }

        // 加入匹配队列中
        roomMatchService.addMatchUser(userID);

        // “阻塞”等待匹配结果（最多等12 * 0.5=6秒）
        for (int i = 0; i < 12; i++) {
            if (redisUtil.hasKey("match_result-" + userID)) {      // 匹配成功
                int roomID = (int) redisUtil.get("match_result-" + userID);
                return Response.success(Map.of("room_id", roomID));
            } else {      // 等待匹配结果
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // 匹配失败
        return Response.fail(ResponseCode.ROOM_MATCH_FAIL);
    }
}
