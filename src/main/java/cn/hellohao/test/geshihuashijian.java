package cn.hellohao.test;

import org.springframework.util.DigestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class geshihuashijian {

    //盐，用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    public static String getMD5(String str) {
        String base = str +"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    public static void main(String[] args) {
        Date d = new Date();
        System.out.println(DateFormat.getDateInstance().format(d));
        String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);
        java.text.DateFormat format1 = new java.text.SimpleDateFormat("MMddhhmmss");
        String s = format1.format(new Date());

        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(uid+s);
        System.out.println("两天前的日期：" + df.format(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000)));
    }
}
