package cn.hellohao.test;

import cn.hellohao.utils.Print;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/7/29 17:38
 * https://blog.csdn.net/WatsonYee/article/details/82389769
 */
public class FTP {
    public static void main(String[] args) throws Exception{

        //创建FTP客户端，所有的操作都基于FTPClinet
        FTPClient ftp = new FTPClient();
        //连接FTP服务器
        ftp.connect("hellohao.cn",21);
        //如果是需要认证的服务器，就需要账号和密码来登录
        ftp.login("test", "test");
        //获取服务器返回的状态码
        int reply = ftp.getReplyCode();
        System.out.println(reply);
        /*
         * 判断是否连接成功
         * 所有以2开头的代码是正完成响应。
         * FTP服务器将在最终发送一个肯定的完成响应成功完成命令。
         */
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            System.out.println("连接失败");
            return;
        }
        System.out.println("连接成功");
        //返回一个布尔类型的值，来表示是否创建成功
        boolean isCreate = ftp.makeDirectory("/ftp");
        //设置文件上传类型
        ftp.setFileType(ftp.BINARY_FILE_TYPE);
        //获取文件的输入流
        FileInputStream fis = new FileInputStream("C:\\Users\\tiansh\\Desktop\\11.jpg");
        //调用storeFile方法将文件上传到FTP服务器
        //第一个参数是上传到服务器的路径，包含了文件名
        boolean isUpload = ftp.storeFile("/ftp/upload22.jpg", fis);
        boolean isUpload2 = ftp.storeFile("/ftp/upload222.jpg", fis);
        Print.warning(isUpload+"=="+isUpload2);
        Print.Normal(isUpload);
        //设置文件权限
        boolean isSet = ftp.sendSiteCommand("chmod 744 " + "/ftp/upload.jpg");
        //ftp.deleteFile("/ftp/upload.jpg");
    }
}
