package com.jgdabc.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 */

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
//                这里指定拦截器的放行路径
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/forget")
                .excludePathPatterns("/admin/login");


    }

    @Override
//    这里离给一个静态资源映射，不然可能我的图片访问不了
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        必须要加的资源映射不然都可能访问不到
        registry.addResourceHandler("templates/**").addResourceLocations("classpath:/templates/");
//        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");

    }
}