package cn.hellohao.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.Base64;
 
/**
 * Created by Hellohao on 2019-08-27.
 */
public class Base64Encryption {

    public static void main(String[] args) {
        toBaseCode("admin");
    }
    /**
     * BASE64加密解密
     */
    public static void toBaseCode(String str) {
        String data = encryptBASE64(str.getBytes());
        System.out.println("sun.misc.BASE64 加密后：" + data);
        System.out.println("sun.misc.BASE64 解密后：" + decryptBASE64(data));
    }
 
    /**
     * BASE64解密
     * @throws Exception
     */
    public static String decryptBASE64(String key)  {
        byte[] b =null;
        try {
            b = (new BASE64Decoder()).decodeBuffer(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new String(b);
    }
 
    /**
     * BASE64加密
     */
    public static String encryptBASE64(byte[] key)  {

        String string = "SGVsbG9oYW8K";
        System.out.println(string.replaceAll("\r|\n",""));

        return (new BASE64Encoder()).encodeBuffer(key).replaceAll("\r|\n", "");
    }
}