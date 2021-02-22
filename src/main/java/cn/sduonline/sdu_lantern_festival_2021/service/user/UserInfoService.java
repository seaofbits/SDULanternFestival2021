package cn.sduonline.sdu_lantern_festival_2021.service.user;

import cn.sduonline.sdu_lantern_festival_2021.dao.UserMapper;
import cn.sduonline.sdu_lantern_festival_2021.entity.mysql.User;
import cn.sduonline.sdu_lantern_festival_2021.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserMapper userMapper;

    public String getNicknameByID(long userID) {
        // 是否有缓存？
        if (redisUtil.hasKey("nickname-" + userID)) {
            return (String) redisUtil.get("nickname-" + userID);
        }
        // 查询
        User user = userMapper.getUserByID(userID);
        if (user == null) {
            // 查询失败
            return "";
        }
        redisUtil.set("nickname-" + userID, user.getNickname());
        return user.getNickname();
    }

    public void setUserNickname(long userID, String userNickname) {
        // 更新缓存+数据库
        redisUtil.set("nickname-" + userID, userNickname);
        User newUser = new User();
        newUser.setUserID(userID);
        newUser.setNickname(userNickname);
        userMapper.setUserNickname(newUser);
    }
}
