package cn.mq.tbed.utils;

import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

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

            Decoder decoder = Base64.getDecoder();
            b = decoder.decode(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new String(b);
    }
    public static String encryptBASE64(byte[] key)  {
        String string = "SGVsbG9oYW8K";
        Encoder encoder = Base64.getEncoder();
        byte[] encode = encoder.encode(key);
        return new String(encode);
    }
}