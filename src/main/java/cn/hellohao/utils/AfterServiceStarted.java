package cn.hellohao.utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AfterServiceStarted implements ApplicationRunner {

    /**
     * 会在服务启动完成后立即执行
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Print.Normal("================================================");
        Print.Normal("||   Hellohao-Pro图床启动成功                ||");
        Print.Normal("||   作者博客：Http://www.hellohao.cn        ||");
        Print.Normal("||   也许这将是最好用的图床                   ||");
        Print.Normal("=================================================");
        //getLinuxLocalIp();

    }



}