package cn.hellohao.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.Base64;
 
/**
 * Created by Hellohao on 2019-08-27.
 */
public class Base64Encryption {
    public static void toBaseCode(String str) {
        String data = encryptBASE64(str.getBytes());
    }
    public static String decryptBASE64(String key)  {
        byte[] b =null;
        try {
            b = (new BASE64Decoder()).decodeBuffer(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  new String(b);
    }
    public static String encryptBASE64(byte[] key)  {
        String string = "SGVsbG9oYW8K";
        return (new BASE64Encoder()).encodeBuffer(key).replaceAll("\r|\n", "");
    }
}