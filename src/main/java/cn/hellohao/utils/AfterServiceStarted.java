package cn.hellohao.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AfterServiceStarted implements ApplicationRunner {

    /**
     * 会在服务启动完成后立即执行
     */
    @Value("${spring.datasource.url}")
    private  String url;
    @Value("${spring.datasource.username}")
    private  String userName;
    @Value("${spring.datasource.password}")
    private  String userPassword;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Print.Normal("______________________________________________");
//        Print.Normal("              Hellohao-Pro                ");
//        Print.Normal("     Successful startup of the program      ");
//        Print.Normal("     is OK!  Open http://your ip:port       ");
//        Print.Normal("______________________________________________");
        //getLinuxLocalIp();
        //System.out.println("开始执行数据库~~");
       // JDBCTool.caozuo(url,userName,userPassword);
    }



}