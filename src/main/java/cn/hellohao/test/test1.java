package cn.hellohao.test;

import cn.hellohao.utils.Print;

import java.io.File;

public class test1 {
    private static void deleteDirectory(File file) {
        if (file.isFile()) {// 表示该文件不是文件夹
            file.delete();
        } else {
            // 首先得到当前的路径
            String[] childFilePaths = file.list();
            for (String childFilePath : childFilePaths) {
                String filePath =File.separator + "HellohaoData" + File.separator;
                File childFile = new File(filePath  + "admin"+ File.separator);
                deleteDirectory(childFile);
            }
            file.delete();
        }
    }

    public static void main(String[] args) {

        Print.warning(System.getProperty("user.dir"));

    }
}
