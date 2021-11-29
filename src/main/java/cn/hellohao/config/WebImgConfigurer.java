package cn.hellohao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;


@Configuration
public class WebImgConfigurer implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String filePath = GlobalConstant.LOCPATH + File.separator;
        registry.addResourceHandler("/ota/**").addResourceLocations("file:"+filePath);
    }


}
