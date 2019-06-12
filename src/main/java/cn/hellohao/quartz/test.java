package cn.hellohao.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        Date d = new Date();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String oldtime = df.format(new Date(d.getTime()));
        System.out.println(df.format(new Date()));
    }
}
