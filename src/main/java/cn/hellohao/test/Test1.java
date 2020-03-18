package cn.hellohao.test;

import cn.hellohao.utils.ImgUrlUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Test1 {



    public static void main(String[] args) {
    	ThreadTest1 t1 = new ThreadTest1();
        ThreadTest1 t2 = new ThreadTest1();
        ThreadTest1 t3 = new ThreadTest1();
        ThreadTest1 t4 = new ThreadTest1();
        ThreadTest1 t5 = new ThreadTest1();
        ThreadTest1 t6 = new ThreadTest1();
        ThreadTest1 t7 = new ThreadTest1();
        ThreadTest1 t8 = new ThreadTest1();
        ThreadTest1 t9 = new ThreadTest1();
        ThreadTest1 t10 = new ThreadTest1();
    	//ThreadTest2 t2 = new ThreadTest2();
 
        Thread thread1 = new Thread(t1);
        Thread thread2 = new Thread(t2);
        Thread thread3 = new Thread(t3);
        Thread thread4 = new Thread(t4);
        Thread thread5 = new Thread(t5);
        Thread thread6 = new Thread(t6);
        Thread thread7 = new Thread(t7);
        Thread thread8 = new Thread(t8);
        Thread thread9 = new Thread(t9);
        Thread thread10 = new Thread(t10);
        thread1.start(); 
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
        thread10.start();

    }
}
 
class ThreadTest1 implements Runnable { 
    public void run() {
        for (int i = 0; i < 999999999; i++) {
             long a = 693;
            System.out.println("流量刷取情况：" +shuashua()+", 已经执行："+ i+"次");
        }
    }
    public static boolean shuashua(){
        String uuid= UUID.randomUUID().toString().replace("-", "");
        boolean bl = false;
        try {
            bl = ImgUrlUtil.downLoadFromUrl("https://ftp.bmp.ovh/imgs/2020/03/6bf4d7c231e6fab6.jpg",
                    uuid, "D://hellohaotmp"+ File.separator);
        } catch (IOException e) {
            e.printStackTrace();
        }
        deleteFile("D://hellohaotmp"+ File.separator+uuid);
        return bl;
    }
    public static boolean deleteFile(String pathname){
        boolean result = false;
        File file = new File(pathname);
        if (file.exists()) {
            file.delete();
            result = true;
            System.out.println("文件已经被成功删除");
        }
        return result;
    }

}
 
class ThreadTest2 implements Runnable { 
    public void run() {
        for (int i = 0; i < 20; i++) {
            System.out.println("ThreadTest2正在运行==========" + i);
        }
    }
}