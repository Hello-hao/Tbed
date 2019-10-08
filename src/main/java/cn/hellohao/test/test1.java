package cn.hellohao.test;


import cn.hellohao.utils.Print;
import cn.hutool.crypto.SecureUtil;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.SecureRandom;
import java.util.Date;

public class test1 {
    public static void main(String[] args) throws  Exception {
        readFile(File.separator + "HellohaoData"+File.separator+"img.ini");
        //charu("Hellohao/b3da10930021007.png|");
    }

    public static void charu(String src) {
        String filePath =File.separator + "HellohaoData" ;
        File file = new File(filePath);
        File file1 = new File(filePath+File.separator+"img.ini");
        if(!file.exists()){
            file.mkdirs();
        }
        if(!file1.exists()){
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            fw = new FileWriter(file1, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(src);//插入每行的内容
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 读取文件内容
    public static String readFile(String path) {//路径
        File file = new File(path);
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
                Print.Normal( s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
