package cn.hellohao;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;

import cn.hellohao.utils.Print;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.util.Scanner;

@SpringBootApplication
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class TbedApplication {

public static void main(String[] args) {
    SpringApplication.run(TbedApplication.class, args);

    Scanner scanner = new Scanner(System.in);
//    Print.Normal("请输入密钥：");
//        String in = scanner.nextLine();
//        if(in.equals("111")){
//            Print.Normal("正确，进入");
//        }else{
//            Print.warning("错误，退出");
//        }



    }
    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("102400KB"); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }

}

