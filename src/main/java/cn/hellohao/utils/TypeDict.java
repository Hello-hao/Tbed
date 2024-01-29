package cn.hellohao.utils;

import cn.hellohao.pojo.Msg;
import org.apache.tika.Tika;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TypeDict {

    //apache大法
    public static Msg FileMiME(InputStream stream){
        final Msg msg = new Msg();
        try {
            Tika tika = new Tika();
            String fileType = tika.detect(stream);
            if (fileType != null && fileType.contains("/")) {
                if(fileType.contains("image/")){
                    msg.setData(fileType);
                }else{
                    //非图像类型
                    msg.setCode("110602");
                    msg.setInfo("该文件非图像文件，或不受支持");
                }
            }
        } catch (Exception e) {
            System.err.println("这是一个图像类别鉴定的报错:161");
            msg.setCode("110603");//图像格式不受支持
            msg.setInfo("暂时不能上传该文件");
        }
        return msg;
    }



}