package cn.sduonline.sdu_lantern_festival_2021.restcontroller.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class LoginController {
    @PostMapping(path = "/login")
    public String login(@RequestParam("user_id") long userID,
                        @RequestParam("password") String password) {
        return "AAAAAAAAAAAAAAA";
    }
}
