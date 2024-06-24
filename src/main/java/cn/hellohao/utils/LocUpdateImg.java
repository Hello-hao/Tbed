package cn.hellohao.utils;

import cn.hellohao.config.GlobalConstant;
import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.ReturnImage;
import cn.hellohao.service.impl.KeysServiceImpl;

import java.io.*;
import java.util.Map;

public class LocUpdateImg {
    public static boolean deleteLOCImg(Images images){
        boolean isDele = false;
        try {
            String filePath = GlobalConstant.LOCPATH + File.separator+images.getImgname();
            File file = new File(filePath);
            if(file.exists()){
                isDele = file.delete();
            }else{
                isDele = true;
            }
        }catch (Exception e){
            e.printStackTrace();
            isDele = false;
        }
        return isDele;
    }

  public static ReturnImage ImageuploadLOC(
      Map<Map<String, String>, File> fileMap, String username, Integer keyID) {
        ReturnImage returnImage = new ReturnImage();
        String filePath = GlobalConstant.LOCPATH + File.separator;
        try {
          for (Map.Entry<Map<String, String>, File> entry : fileMap.entrySet()) {
            String prefix = entry.getKey().get("prefix");
            String ShortUIDName = entry.getKey().get("name");
            File dest = new File(filePath + username + File.separator + ShortUIDName + "." + prefix);
            if (!dest.getParentFile().exists()) {
              dest.getParentFile().mkdirs();
            }
            InputStream fileInputStream = new FileInputStream(entry.getValue());
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dest));
            byte[] bs = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bs)) != -1) {
              bos.write(bs, 0, len);
            }
              try {
                  bos.flush();
                  if(bos!=null){
                      bos.close();
                  }
                  if(fileInputStream!=null){
                      fileInputStream.close();
                  }
              }catch (Exception e){}
          }
          returnImage.setCode("200");
        } catch (Exception e) {
          e.printStackTrace();
          returnImage.setCode("500");
          System.err.println("上传失败");
        }
        return returnImage;
    }

}
