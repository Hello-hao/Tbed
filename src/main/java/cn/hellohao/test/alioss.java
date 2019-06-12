package cn.hellohao.test;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

public class alioss {
    //上传文件
public static void shangchuan() throws Exception{
    // Endpoint以杭州为例，其它Region请按实际情况填写。
    String endpoint = "http://oss-cn-beijing.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    String accessKeyId = "LTAI0Yd6Li0VxrUF";
    String accessKeySecret = "F2ZuDMq0IYefvVDKyfJLpRNzXldci9";
    String bucketName = "hellohao";
    //String objectName = "hellohao.png";
//设置Header
    ObjectMetadata meta = new ObjectMetadata();
    meta.setHeader("Content-Type", "image/jpeg");//image/jpeg
    meta.setHeader("Content-Disposition", "inline");

// 创建OSSClient实例。
    OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

// 上传文件流。
    InputStream inputStream = new FileInputStream("D:\\桌面\\资料\\TouXiang.png");
    //ossClient.putObject(bucketName, "test/TouXiang.png", inputStream,meta);
    ossClient.deleteObject(bucketName, "test/TouXiang.png");
// 关闭OSSClient。
    ossClient.shutdown();
}

    public static void main(String[] args) throws Exception{
        alioss.shangchuan();
        //image/jpeg
    }


    public void settou(){

        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI0Yd6Li0VxrUF";
        String accessKeySecret = "F2ZuDMq0IYefvVDKyfJLpRNzXldci9";

        //String content = "hellohao";

// 创建上传文件的元信息，可以通过文件元信息设置HTTP header。
        ObjectMetadata meta = new ObjectMetadata();

        //String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(content.getBytes()));
// 开启文件内容MD5校验。开启后OSS会把您提供的MD5与文件的MD5比较，不一致则抛出异常。
        //meta.setContentMD5(md5);
// 指定上传的内容类型。内容类型决定浏览器将以什么形式、什么编码读取文件。如果没有指定则根据文件的扩展名生成，如果没有扩展名则为默认值application/octet-stream。
        meta.setContentType("text/plain");
// 设置内容被下载时的名称。
        meta.setContentDisposition("attachment; filename=\"DownloadFilename\"");
// 设置上传文件的长度。如超过此长度，则会被截断，为设置的长度。如不足，则为上传文件的实际长度。
        //meta.setContentLength(content.length());
// 设置内容被下载时网页的缓存行为。
        meta.setCacheControl("Download Action");
// 设置缓存过期时间，格式是格林威治时间（GMT）。
        //meta.setExpirationTime(DateUtil.parseIso8601Date("2022-10-12T00:00:00.000Z"));
// 设置内容被下载时的编码格式。
        meta.setContentEncoding("utf-8");
// 设置header。
        meta.setHeader("<yourHeader>", "<yourHeaderValue>");
// 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

// 上传文件。
        //ossClient.putObject("<yourBucketName>", "<yourObjectName>", new ByteArrayInputStream(content.getBytes()), meta);

// 关闭OSSClient。
        ossClient.shutdown();
    }

}


