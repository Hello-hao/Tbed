package cn.hellohao.test;

import com.UpYun;

import java.io.File;

public class Uppai {
    public static void chushihua() throws Exception{
        UpYun upyun = new UpYun("hellohao1", "hellohao", "gXDQKvAHBvtYEirLoSjFThxrOHFaUsOz");
        //设置超时时间
        upyun.setTimeout(60);
        String path = "/test1/1.png";

        // 例2：采用数据流模式上传文件（节省内存）,自动创建父级目录
//        File file = new File("D:\\桌面\\资料\\TouXiang.png");
//        upyun.setContentMD5(UpYun.md5(file));
//        boolean result = upyun.writeFile(path, file, true);

        // 删除目录

        //boolean result = upyun.rmDir(path);
        boolean result = upyun.deleteFile(path, null);
        System.out.println(result);
    }
    public static void main(String[] args) throws Exception {
        Uppai.chushihua();
    }
}
