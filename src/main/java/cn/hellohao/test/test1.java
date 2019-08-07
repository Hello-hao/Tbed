package cn.hellohao.test;

import cn.hellohao.utils.Print;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;

import java.io.File;
import java.io.IOException;

public class test1 {

    public static void main(String[] args) {


        String str = "hellohao.cn:888";
        int flag = str.indexOf(":");
        System.out.println(flag);


    }
}
