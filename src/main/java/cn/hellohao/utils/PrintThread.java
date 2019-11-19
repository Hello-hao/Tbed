package cn.hellohao.utils;

import java.util.UUID;

public class PrintThread extends Thread {

    public Integer xc;
    public PrintThread(Integer xc) {
        this.xc = xc;
    }

    @Override

    public void run() {
        for(int i=1;i<99999999;i++){
            //String intFlag = String.valueOf((int)(Math.random() * 1000000));
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,9);//生成一个没有-的uuid，然后取前5位
            //sendPost("http://shenhhh.net/index.php?m=User&a=signup","phone=13232"+intFlag+"&password=asd"+intFlag+"",i);
            String ret = demo.sendPost("https://test.demo-1s.com/register","name="+uuid+"&email="+uuid+"@jbb.com&password="+uuid,i);
            System.out.println("线程 【"+xc+"】 ：账号：goto"+uuid+"-- 密码：baa"+uuid+"-- 邮箱：jbb"+uuid+"@jbb.com  -- 返回值："+ret);
        }
    }

    public static void main(String[] args) {
        new PrintThread(1).start();
        new PrintThread(2).start();
        new PrintThread(3).start();
        new PrintThread(4).start();
        new PrintThread(5).start();
    }
}