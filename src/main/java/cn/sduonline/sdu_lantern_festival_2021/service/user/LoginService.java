package cn.sduonline.sdu_lantern_festival_2021.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class LoginService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * 用户是否有已经拥有一个token
     *
     * @param userID 用户id
     * @return 如果用户原来有token，返回该token，否则返回{@code null}
     */
    public String userHasToken(long userID) {
        return stringRedisTemplate.opsForValue().get("token-" + userID);
    }


    /**
     * 获取用户的token，有就返回原来的那个，没有则新建一个。
     *
     * @param userID 用户id
     * @return 用户的token
     */
    public String createToken(long userID) {
        // 新建一个token
        String token = randomString(16);
        // token有效期两小时
        stringRedisTemplate.opsForValue().set("token-" + token,
                String.valueOf(userID), 1000 * 60 * 60 * 2, TimeUnit.MILLISECONDS);
        // 建立从user找token的映射
        stringRedisTemplate.opsForValue().set("token-" + userID, token,
                1000 * 60 * 60 * 2, TimeUnit.MILLISECONDS);
        return token;
    }

    static final String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
    static final Random rnd = new Random();

    static String randomString(int len) {
        char[] ch = new char[len];
        for (int i = 0; i < len; i++)
            ch[i] = AB.charAt(rnd.nextInt(AB.length()));
        return new String(ch);
    }
}
