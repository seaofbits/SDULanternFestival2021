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

    static final String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
    static final Random rnd = new Random();

    String randomString(int len) {
        char[] ch = new char[len];
        for (int i = 0; i < len; i++)
            ch[i] = AB.charAt(rnd.nextInt(AB.length()));
        return new String(ch);
    }

    /**
     * 获取用户的token，有就返回原来的那个，没有则新建一个。
     *
     * @param userID 哪个用户
     * @return 用户的token
     */
    public String getToken(long userID) {
        // 检查是否有原来的token
        String idKey = "token-" + userID;
        if (stringRedisTemplate.hasKey(idKey))
            return stringRedisTemplate.opsForValue().get(idKey);

        // 没有token，要新建一个
        String token = randomString(16);
        // token有效期两小时
        stringRedisTemplate.opsForValue().set("token-" + token,
                String.valueOf(userID), 1000 * 60 * 60 * 2, TimeUnit.MILLISECONDS);
        // 建立从user找token的映射
        stringRedisTemplate.opsForValue().set(idKey, token,
                1000 * 60 * 60 * 2, TimeUnit.MILLISECONDS);
        return token;
    }
}
