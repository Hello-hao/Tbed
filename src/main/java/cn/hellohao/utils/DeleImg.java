package cn.hellohao.utils;


import java.io.*;

public class DeleImg {
    public static void charu(String imgUrlText) {
        String filePath =File.separator + "HellohaoData" ;
        File file = new File(filePath);
        File file1 = new File(filePath+File.separator+"img.ini");
        if(!file.exists()){
            file.mkdirs();
        }
        if(!file1.exists()){
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = null;
        try {
            //如果文件存在，则追加内容；如果文件不存在，则创建文件
            fw = new FileWriter(file1, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(fw);
        pw.println(imgUrlText);
        pw.flush();
        try {
            fw.flush();
            pw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 读取文件内容
    public static String readFile(String path) {//路径
        File file = new File(path);
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
                Print.Normal( s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
