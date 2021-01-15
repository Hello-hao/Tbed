package cn.mq.tbed.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class SetFiles {
    // 转换文件方法
    public static File changeFile(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getName();//multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // todo 修改临时文件文件名
        File file = null;
        try {
            file = File.createTempFile(fileName, prefix);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    // 转换文件方法
    public static File changeFile_new(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();//getOriginalFilename
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // todo 修改临时文件文件名
        File file = null;
        try {
            file = File.createTempFile(fileName, prefix);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    //判断文件是否是图片格式
    public static Boolean VerifyImage(MultipartFile multipartFile){
        byte[] b = new byte[3];
        FileInputStream fis = null;
        String suf = null;
        try {
            fis = new FileInputStream(SetFiles.changeFile(multipartFile).getPath());
            fis.read(b, 0, b.length);
            suf = ImgUrlUtil.bytesToHexString(b);
            suf = suf.toUpperCase();
            if(fis!=null){fis.close();}
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(TypeDict.checkType(suf).equals("0000")) {
            return false;
        }else{
            return true;
        }
    }



}
