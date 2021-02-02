package cn.sduonline.sdu_lantern_festival_2021.config;

import cn.sduonline.sdu_lantern_festival_2021.restcontroller.login_inspector.LoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 1. 配置好拦截器需要拦截那些请求（见LoginInterceptor.java）
// 2. 把拦截器放在容器中（操作如下）
// 3. 指定拦截规则

@Configuration
public class LoginInspectorConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getLoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login");
    }
}
