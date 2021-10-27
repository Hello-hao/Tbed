package cn.hellohao.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DecimalFormat;

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
    public static File changeFile_c(MultipartFile multipartFile) {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();//multipartFile.getOriginalFilename();
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

    //文件大小单位转换
    public static String readableFileSize(long fileS) {
        if(fileS==0){
            return "0B";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            if(fileS < 1099511627776L){
                fileSizeString = df.format((double) fileS / 1073741824) + "GB";
            }else{
                fileSizeString = df.format((double) fileS / 1099511627776L) + "TB";
            }
        }
        return fileSizeString;
    }




}
