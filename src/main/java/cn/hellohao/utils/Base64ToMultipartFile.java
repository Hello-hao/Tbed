package cn.hellohao.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

/**
 * base64转为multipartFile工具类
 * base64Convert
 */

public class Base64ToMultipartFile implements MultipartFile {
    private final byte[] imgContent;
    private final String header;



    public Base64ToMultipartFile (byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }

    @Override
    public String getName() {
        return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
    }

    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + (int) Math.random() * 10000 + "." + header.split("/")[1];
    }

    @Override
    public String getContentType() {
        return header.split(":")[1];
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
       ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imgContent);
		// 关闭流，不然文件会一直占用资源，无法对其操作
		byteArrayInputStream.close();
		return byteArrayInputStream;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
      	FileOutputStream fileOutputStream = new FileOutputStream(dest);
		fileOutputStream.write(imgContent);
		// 关闭流，不然文件会一直占用资源，无法对其操作
		fileOutputStream.close();
    }

    /**
     * base64转multipartFile
     *
     * @param base64
     * @return
     */
    public static MultipartFile base64Convert(String base64) {
//        String[] baseStrs = base64.split(",");
//        BASE64Decoder decoder = new BASE64Decoder();
//        byte[] b = new byte[0];
//        try {
//            b = decoder.decodeBuffer(baseStrs[1]);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (int i = 0; i < b.length; ++i) {
//            if (b[i] < 0) {
//                b[i] += 256;
//            }
//        }
//        return new Base64ToMultipartFile (b, baseStrs[0]);

        String[] baseStrs = base64.split(",");
        byte[] b = Base64.getDecoder().decode(baseStrs[1]);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        return new Base64ToMultipartFile(b, baseStrs[0]);

    }




}

