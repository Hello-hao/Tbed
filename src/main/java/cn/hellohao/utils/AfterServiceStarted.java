package cn.hellohao.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AfterServiceStarted implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Print.Normal("______________________________________________");
    }


}