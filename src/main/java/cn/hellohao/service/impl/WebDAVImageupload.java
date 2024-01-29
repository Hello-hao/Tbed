package cn.hellohao.service.impl;

import cn.hellohao.config.GlobalConstant;
import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.Msg;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.service.ImgService;
import cn.hellohao.service.KeysService;
import cn.hellohao.utils.TypeDict;
import com.github.sardine.impl.methods.HttpMkCol;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class WebDAVImageupload {

    static CloseableHttpClient httpClient;
    static Keys key;

    @Autowired
    private ImgService imgService;
    @Autowired
    private KeysService keysService;
    private static Logger logger = LoggerFactory.getLogger(WebDAVImageupload.class);
    public ReturnImage ImageuploadWebDAV(Map<Map<String, String>, File> fileMap, String username,Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        try {
            if(null==httpClient || null==key){
                System.out.println("WebDAV = 存储源对象为空");
                returnImage.setCode("500");
                return returnImage;
            }
        } catch (Exception e) {
            logger.error("ImageuploadWebDAV:61异常",e);
            System.out.println("WebDAV = 存储源对象为空");
            returnImage.setCode("500");
            return returnImage;
        }
        File file = null;
        try {
            for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
                String prefix = entry.getKey().get("prefix");
                String ShortUIDName = entry.getKey().get("name");
                file = entry.getValue();
                createDirectories(httpClient, key.getEndpoint(),  "/" + username);
                HttpPut httpPut = new HttpPut(key.getEndpoint() + "/" + username + "/" + ShortUIDName + "." + prefix);
                FileEntity fileEntity = new FileEntity(file, ContentType.DEFAULT_BINARY);
                httpPut.setEntity(fileEntity);
                HttpResponse response = httpClient.execute(httpPut);
                final int statusCode = response.getStatusLine().getStatusCode();
                logger.info("WebDav上传响应代码："+statusCode);
                EntityUtils.consume(response.getEntity());
                if(statusCode >= 200 && statusCode < 300){
                    returnImage.setCode("200");
                }else{
                    returnImage.setCode(Integer.toString(statusCode));
                }
            }

        } catch (Exception e) {
            logger.error("ImageuploadWebDAV:87异常",e);
            returnImage.setCode("500");
        }
        return returnImage;
    }

    //webdav初始化
    public static Integer Initialize(Keys k) {
        int ret = -1;
        try {
            if(k.getSysTransmit()==false){
                if (StringUtils.isBlank(k.getAccessKey())
                        || StringUtils.isBlank(k.getAccessSecret())
                        || StringUtils.isBlank(k.getEndpoint())
                        || StringUtils.isBlank(k.getRequestAddress())) {
                    return -1;
                }
            }else{
                if (StringUtils.isBlank(k.getAccessKey())
                        || StringUtils.isBlank(k.getAccessSecret())
                        || StringUtils.isBlank(k.getEndpoint())) {
                    return -1;
                }
            }
            String endpoint = k.getEndpoint();
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials(k.getAccessKey(), k.getAccessSecret()));
            CloseableHttpClient httpClientObj = HttpClients.custom()
                    .setDefaultCredentialsProvider(credentialsProvider)
                    .build();
            HttpUriRequest request = RequestBuilder.create("PROPFIND")
                    .setUri(endpoint +"/"+ k.getRootPath())
                    .addHeader("Depth", "1")
                    .setEntity(new StringEntity(
                            "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n" +
                                    "<D:propfind xmlns:D=\"DAV:\">\n" +
                                    "  <D:allprop/>\n" +
                                    "</D:propfind>",
                            ContentType.create("text/xml", "UTF-8")))
                    .build();
            HttpResponse response = httpClientObj.execute(request);
            System.out.println("=====>"+response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode()==207 || response.getStatusLine().getStatusCode()==404){
                ret = 1;
                httpClient = httpClientObj;
                key = k;
            }else {
                return -1;
            }
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            logger.error("WebDAV初始化失败", e);
            return -1;
        }
        return ret;
    }

    public Boolean delWebDAV(Integer keyID, Images images) {
        boolean b = true;
        try {
            Keys keys = keysService.selectKeys(images.getSource());
            String rootPath = keys.getRootPath().equals("/")?"":keys.getRootPath();
            String fileUrl = keys.getEndpoint()+"/"+images.getImgname();
            System.err.println("删除缩yuan图："+fileUrl);
            HttpDelete httpDelete = new HttpDelete(fileUrl);
            HttpResponse response = httpClient.execute(httpDelete);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 204) {
                logger.info("File deleted: " + fileUrl);
//            checkAndDeleteEmptyParentDirectories(httpClient,fileUrl);
            } else {
                logger.error("Failed to delete file: " + fileUrl);
            }
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            logger.error("WebDav删除异常：",e);
            b = false;
        }
        return b;
    }


    public ResponseEntity<InputStreamResource> getWebDAV(String shortUuid) {
        boolean b = true;
        try {
            Images images = null;
            images = imgService.selectImgByShortlink(shortUuid);
            Keys keys = keysService.selectKeys(images.getSource());
            String rootPath = keys.getRootPath().equals("/")?"":keys.getRootPath();
            String fileUrl = keys.getEndpoint()+"/"+images.getImgname();
            HttpGet httpGet = new HttpGet(fileUrl);
            HttpResponse response = httpClient.execute(httpGet);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "image/jpeg");
                return ResponseEntity.ok()
                        .headers(headers)
                        .body(new InputStreamResource(response.getEntity().getContent()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private static void createDirectories(CloseableHttpClient httpClient, String baseUrl, String directoryPath) throws Exception {
        String[] directories = directoryPath.split("/");
        String currentPath = baseUrl;
        for (String directory : directories) {
            currentPath += directory + "/";
            HttpMkCol httpMkCol = new HttpMkCol(currentPath);
            HttpResponse response = httpClient.execute(httpMkCol);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 201) {
                System.out.println("Directory created: " + currentPath);
            } else if (statusCode == 405) {
                System.out.println("Directory already exists: " + currentPath);
            } else {
                System.err.println("Failed to create directory: " + currentPath);
            }
            EntityUtils.consume(response.getEntity());
        }
    }


}
