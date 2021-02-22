package cn.sduonline.sdu_lantern_festival_2021.restcontroller.user;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector.Auth;
import cn.sduonline.sdu_lantern_festival_2021.service.user.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@ResponseBody
public class UserNicknameController {
    @Autowired
    UserInfoService userInfoService;

    @Auth
    @GetMapping(path = "/user/nickname")
    public Response getAttr(@RequestAttribute("user_id") long userID) {
        return Response.success(Map.of("nickname", userInfoService.getNicknameByID(userID)));
    }

    @Auth
    @PostMapping(path = "/user/nickname")
    public Response getAttr(@RequestParam("nickname") String nickname,
                            @RequestAttribute("user_id") long userID) {
        if (nickname.equals("")) {
            return Response.fail(ResponseCode.ERROR_PARAMETER_INVALID_VALUE, "nickname不能为空字符串");
        }
        userInfoService.setUserNickname(userID, nickname);
        return Response.success();
    }
}
