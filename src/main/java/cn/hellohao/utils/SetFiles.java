package cn.hellohao.utils;

import cn.hellohao.pojo.Chunk;
import cn.hellohao.pojo.Msg;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.stream.Stream;

public class SetFiles {
    private static Logger logger = LoggerFactory.getLogger(SetFiles.class);
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
        String fileName = multipartFile.getOriginalFilename();//getOriginalFilename
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // todo 修改临时文件文件名
        File file = null;
        try {
            String new_FileName = System.getProperty("java.io.tmpdir")+"hellohao_tmp_upload"+File.separator+fileName;
            file = new File(new_FileName);
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
        } catch (IOException e) {
            logger.error("SetFiles.changeFile_new", e);
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

    public static String generatePath(String uploadFolder, Chunk chunk) {
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFolder+File.separator).append("/").append(chunk.getIdentifier());
        if (!Files.isWritable(Paths.get(sb.toString()))) {
            System.err.println("path not exist,create path");
            System.err.println(sb.toString());
            try {
                Files.createDirectories(Paths.get(sb.toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.append("/")
                .append(chunk.getFilename())
                .append("-")
                .append(chunk.getChunkNumber()).toString();
    }

    public static Msg merge(String targetFile, String folder, String filename) {
        Msg msg = new Msg();
        try {
            final File file = new File(targetFile);
            if (file.exists()){
                file.delete();
            }
            Files.createFile(Paths.get(targetFile));
        } catch (IOException e) {
            System.out.println("这是一个无伤大雅的错:IOException");
            e.printStackTrace();
        }
        try (Stream<Path> stream = Files.list(Paths.get(folder))) {
            stream.filter(path -> !path.getFileName().toString().equals(filename))
                    .sorted((o1, o2) -> {
                        String p1 = o1.getFileName().toString();
                        String p2 = o2.getFileName().toString();
                        int i1 = p1.lastIndexOf("-");
                        int i2 = p2.lastIndexOf("-");
                        return Integer.valueOf(p2.substring(i2)).compareTo(Integer.valueOf(p1.substring(i1)));
                    })
                    .forEach(path -> {
                        try {
                            //以追加的形式写入文件
                            Files.write(Paths.get(targetFile), Files.readAllBytes(path), StandardOpenOption.APPEND);
                            //合并后删除该块
                            Files.delete(path);
                        } catch (IOException e) {
                            msg.setCode("500");
                            msg.setInfo("合并文件时发生了错误");
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            msg.setCode("500");
            msg.setInfo("合并文件时发生了错误");
            e.printStackTrace();
        }
        return msg;
    }

    public static void delFileFolder(File file){
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File in: files) {
                delFileFolder(in);
            }
        }
        file.delete();
    }

}
