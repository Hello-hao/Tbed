package cn.hellohao.test;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.net.InetAddress;

public class MyAuthenticator extends Authenticator {
    /*在使用Authenticator这个抽象类时，我们必须采用继承该抽象类的方式，并且该继承类必须具
     * 有返回PasswordAuthentication对象（用于存储认证时要用到的用户名、密码）getPasswordAuthentication()
     * 方法。并且要在Session中进行注册，使Session能够了解在认证时该使用哪个类。
     * */
    public static void main(String[] args) throws Exception {
        //获取本机的ip地址和域名
        InetAddress ia = InetAddress.getLocalHost();
        System.out.println(ia.toString());
        System.out.println(ia.getHostName());//域名               127
        System.out.println(ia.getHostAddress());//ip地址           192.168.201.254


    }


}  