package cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector;

import cn.sduonline.sdu_lantern_festival_2021.entity.Response;
import cn.sduonline.sdu_lantern_festival_2021.entity.ResponseCode;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        // 不是方法的handler，放行
        if (!(handler instanceof HandlerMethod))
            return true;

        // 方法没有被@Auth注解，放行
        Method method = ((HandlerMethod) handler).getMethod();
        if (method.getAnnotation(Auth.class) == null) {
            return true;
        }

        // 不存在token或者token过期等等
        String token = request.getHeader("Token");
        if (token == null ||
                !stringRedisTemplate.hasKey("token-" + token)) {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json; charset=UTF-8");
            response.getWriter().println(JSON.toJSONString(Response.fail(ResponseCode.ERROR_NO_TOKEN)));
            return false;
        }
        long userID = Long.parseLong(stringRedisTemplate.opsForValue().get("token-" + token));
        request.setAttribute("user_id", userID);
        return true;
    }
}
