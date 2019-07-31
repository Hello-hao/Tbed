package cn.hellohao.test;

import cn.hellohao.utils.Print;
import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;

import java.io.File;
import java.io.IOException;

public class test1 {

    public static void main(String[] args) {

//匿名登录（无需帐号密码的FTP服务器）
        Ftp ftp = new Ftp("hellohao.cn");
        ftp.init("hellohao.cn",21,"test","test");
//进入远程目录
        //ftp.cd("/opt/upload");
//上传本地文件
        ftp.upload("/ftp/222222222.jpg", FileUtil.file("C:\\Users\\tiansh\\Desktop\\11.jpg"));
//下载远程文件
        //ftp.download("/opt/upload", "test.jpg", FileUtil.file("e:/test2.jpg"));

//关闭连接
        try {
            ftp.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
