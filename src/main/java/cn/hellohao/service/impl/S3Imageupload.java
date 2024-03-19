package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2023/3/15 14:04
 */
@Service
public class S3Imageupload {
    private static Logger logger = LoggerFactory.getLogger(S3Imageupload.class);

    static AmazonS3 AS3;
    static Keys KEY;
    public ReturnImage ImageuploadS3(
            Map<Map<String, String>, File> fileMap, String username, Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        try {
            if (null == AS3 || null == AS3) {
                logger.error("S3 = 存储源对象为空");
                returnImage.setCode("500");
                return returnImage;
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnImage.setCode("500");
            return returnImage;
        }
        File file = null;
        FileInputStream stream = null;
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String prefix = entry.getKey().get("prefix");
                String ShortUIDName = entry.getKey().get("name");
                file = entry.getValue();
                stream = new FileInputStream(file);
                //解决存储源不能浏览图片的问题
//                ObjectMetadata objectMetadata = new ObjectMetadata();
//                objectMetadata.addUserMetadata();
                AS3.putObject(
                        new PutObjectRequest(
                                KEY.getBucketname(),
                                username + "/" + ShortUIDName + "." + prefix,
                                stream,null));
                //        S3Object object = s3.getObject( new GetObjectRequest( key.getBucketname(),
                try {if(stream!=null){stream.close();}} catch (Exception e) { }
            }
            returnImage.setCode("200");
        } catch (Exception e) {
            try {if(stream!=null){stream.close();}} catch (Exception ex) { }
            logger.error("S3协议上传发生异常：",e);
            returnImage.setCode("500");
        }
        return returnImage;
    }


    public static Integer Initialize(Keys k) {
        int ret = -1;
        if (StringUtils.isBlank(k.getAccessKey())
                || StringUtils.isBlank(k.getAccessSecret())
                || StringUtils.isBlank(k.getRootPath())
                || StringUtils.isBlank(k.getEndpoint())
                || StringUtils.isBlank(k.getBucketname())
                || StringUtils.isBlank(k.getRequestAddress())) {
            return -1;
        }
        try {
            String regionStr = k.getRegion() == null ? "" : k.getRegion();
            ClientConfiguration config = new ClientConfiguration();
            config.setMaxConnections(500);
            config.setConnectionTimeout(30);
            // cn-north-1 us-east-1
            BasicAWSCredentials credentials =
                    new BasicAWSCredentials(k.getAccessKey(), k.getAccessSecret());
            AmazonS3 s3 =
                    AmazonS3ClientBuilder.standard()
                            .withClientConfiguration(config)
                            .withPathStyleAccessEnabled(false)
                            .withCredentials(new AWSStaticCredentialsProvider(credentials))
                            .withEndpointConfiguration(
                                    new AwsClientBuilder.EndpointConfiguration(
                                            k.getEndpoint(), regionStr))
                            .build();
            ObjectListing objectListing = s3.listObjects(k.getBucketname());
            ret = 1;
            AS3 = s3;
            KEY = k;
        } catch (Exception e) {
            logger.warn("S3 Object  " + k.getKeyname() + "  Is null");
            ret = -1;
        }
        if(ret==-1){
            ret = Initialize_Tow(k);
            if(ret>-1){
                //OK
            }
        }
        return ret;
    }

    public static Integer Initialize_Tow(Keys k) {
        int ret = -1;
        try {
            ClientConfiguration config = new ClientConfiguration();
            AwsClientBuilder.EndpointConfiguration endpointConfig =
                    new AwsClientBuilder.EndpointConfiguration(k.getEndpoint(), k.getRegion());//cn-north-1
            AWSCredentials awsCredentials = new BasicAWSCredentials(k.getAccessKey(),k.getAccessSecret());
            AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
            AmazonS3 s3  = AmazonS3Client.builder()
                    .withEndpointConfiguration(endpointConfig)
                    .withClientConfiguration(config)
                    .withCredentials(awsCredentialsProvider)
                    .disableChunkedEncoding()
                    .withPathStyleAccessEnabled(true)
                    .build();
            List<Bucket> buckets = s3.listBuckets();
            boolean b = false;
            for (Bucket bucket : buckets) {
                if(k.getBucketname().equals(bucket.getName())){
                    b = true;
                }
            }
            if(b==false){
                throw new NullPointerException();
            }
            ret=1;
            AS3 = s3;
            KEY = k;
        } catch (Exception e) {
            ret = -1;
        }
        return ret;
    }

    public boolean deleS3(Integer keyID, Images images) {
        boolean b = true;
        try {
            DeleteObjectRequest deleteObjectRequest =
                    new DeleteObjectRequest(KEY.getBucketname(), images.getImgname());
            AS3.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }

}
