package cn.hellohao.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.concurrent.TimeUnit;

//@Configuration
public class WebConfigConfigurer extends WebMvcConfigurationSupport {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Print.Normal("修改响应头开始");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                //.setCacheControl(CacheControl.noCache());
                .setCacheControl(
                        CacheControl.maxAge(1, TimeUnit.SECONDS)
                                .cachePublic());
    }



}