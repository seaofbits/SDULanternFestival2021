package cn.sduonline.sdu_lantern_festival_2021.restcontroller.login;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import cn.sduonline.sdu_lantern_festival_2021.service.user.LoginService;
import cn.sduonline.sdu_lantern_festival_2021.service.user.third_party.CasLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
@ResponseBody
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping(path = "/login")
    public Response login(@RequestParam("user_id") long userID,
                          @RequestParam("password") String password) {
        boolean usernameOrPasswordWrong;
        try {
            // 不敢自动包装，扯出些奇奇怪怪的bug，就这样每次new一下吧
            String cookie = new CasLogin().serve(String.valueOf(userID), password);
            usernameOrPasswordWrong = cookie.equals("null");
        } catch (IOException e) {
            e.printStackTrace();
            return Response.fail(ResponseCode.ERROR_UNKNOWN, e.getMessage());
        }

        if (usernameOrPasswordWrong) {
            return Response.fail(ResponseCode.USER_WRONG_ID_OR_PASSWORD);
        }

        // if in 黑名单 return ...

        String token = loginService.getToken(userID);
        return Response.success(Map.of("token", token));
    }
}
