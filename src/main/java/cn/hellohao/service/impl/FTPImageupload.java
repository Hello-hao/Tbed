package cn.hellohao.service.impl;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.utils.FTPUtils;
import cn.hellohao.utils.Print;
import cn.hellohao.utils.SetText;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FTPImageupload {
    static Keys key;

    public ReturnImage ImageuploadFTP(Map<String, File> fileMap, String username,Integer keyID)  {
        ReturnImage returnImage = new ReturnImage();
        String[] host = key.getEndpoint().split("\\:");
        String h = host[0];
        Integer p = Integer.parseInt(host[1]);
        FTPUtils ftps = new FTPUtils(h, p, key.getAccessKey(), key.getAccessSecret());
        boolean flag = ftps.open();
        File file = null;
        Map<ReturnImage, Integer> ImgUrl = new HashMap<>();
        ftps.mkDir(File.separator+username);
        try {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String ShortUID = SetText.getShortUuid();
                file = entry.getValue();
                String userkey =username + "/"+ ShortUID + "." + entry.getKey();
                if (flag) {
                    boolean isUpload = ftps.upload(file, "/" + userkey, "");
                    if(isUpload){
                        returnImage.setImgname(userkey);
                        returnImage.setImgurl(key.getRequestAddress() + "/"+ userkey);
                        returnImage.setImgSize(entry.getValue().length());
                        returnImage.setCode("200");
                    }else{
                        returnImage.setCode("500");
                    }
                }
                Print.Normal("要上传的文件路径：/"+userkey);
            }
        }catch (Exception e){
            e.printStackTrace();
            returnImage.setCode("500");
        }
        return returnImage;
    }

    public static Integer Initialize(Keys k) {
        int ret = -1;
        if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null && k.getRequestAddress()!=null ) {
            if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("") && !k.getRequestAddress().equals("") ) {
                FTPClient ftp = new FTPClient();
                ftp.setConnectTimeout(0);
                int flag = k.getEndpoint().indexOf(":");
                if(flag>0){
                    String[] host = k.getEndpoint().split("\\:");
                    String h = host[0];
                    Integer p = Integer.parseInt(host[1]);
                    try {
                        if(!ftp.isConnected()){
                            ftp.connect(h,p);
                        }
                        ftp.login(k.getAccessKey(), k.getAccessSecret());
                        int reply = ftp.getReplyCode();
                        if (!FTPReply.isPositiveCompletion(reply)) {
                            System.out.println("FTP Object Is null");
                            ftp.disconnect();
                            return -1;
                        }
                        ret = 1;
                        key = k;
                    } catch (IOException e) {
                        System.out.println("FTP Object Is null");
                        return -1;
                    }
                }
            }
        }
        return ret;
    }

    public static void fuzhi(String p1,String p2){
        String a=p1;
        try{
            File afile=new File(a);
            File bfile=new File(p2);//定义一个复制后的文件路径
            bfile.createNewFile();
            FileInputStream c=new FileInputStream(afile);
            FileOutputStream d=new FileOutputStream(bfile);
            byte[] date=new byte[512];
            int i=0;
            while((i=c.read(date))>0){
                d.write(date);
            }
            c.close();
            d.close();
            System.out.println("文件复制成功");}
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public Boolean delFTP(Integer keyID, Images images) {
        boolean b = true;
        try {
            String[] host = key.getEndpoint().split("\\:");
            String h = host[0];
            Integer p = Integer.parseInt(host[1]);
            //创建FTP客户端，所有的操作都基于FTPClinet
            FTPUtils ftps = new FTPUtils(h, p, key.getAccessKey(), key.getAccessSecret());
            ftps.open();
            b = ftps.deleteFile(images.getImgname());
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }


}
