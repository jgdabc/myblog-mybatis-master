package com.jgdabc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器失效

@Component
@Configuration
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
      log.info("获取到请求路径--{}",request.getRequestURL());
        if (request.getRequestURL().equals("lo"))
            if (request.getSession().getAttribute("user") == null) {
                response.sendRedirect("/admin");
                return false;
            }
        return true;
    }
}