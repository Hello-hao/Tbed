package cn.hellohao.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class SetFiles {
    // 转换文件方法
    public static File changeFile(MultipartFile multipartFile) throws Exception {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // todo 修改临时文件文件名
        File file = File.createTempFile(fileName, prefix);
        multipartFile.transferTo(file);
        return file;
    }
}
