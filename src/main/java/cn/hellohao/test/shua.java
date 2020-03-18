package cn.hellohao.test;

import cn.hellohao.utils.ImgUrlUtil;
import cn.hellohao.utils.Print;
import jdk.nashorn.internal.objects.annotations.Where;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020-03-11 21:45
 */
public class shua {


    public static void main(String[] args) {

        for (int i = 0; i <999999999 ; i++) {
            shuashua();
            Print.Normal("已成功执行："+i+" 次");
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
        return bl;
    }

    public static Long jia(){
        long l = 693;
        return l++;
    }
}
