package cn.mq.tbed.utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AfterServiceStarted implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Print.Normal("______________________________________________");
    }


}