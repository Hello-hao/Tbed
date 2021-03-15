package cn.hellohao.service.impl;

import cn.hellohao.controller.UpdateImgController;
import cn.hellohao.dao.*;
import cn.hellohao.pojo.*;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020/1/9 15:46
 */

@Service
public class UploadServicel {
    @Autowired
    ConfigMapper configMapper;
    @Autowired
    UploadConfigMapper uploadConfigMapper;
    @Autowired
    KeysMapper keysMapper;
    @Autowired
    ImgMapper imgMapper;
    @Autowired
    UserMapper userMapper;


    public Msg uploadForLoc(HttpSession session, HttpServletRequest request,
                            MultipartFile multipartFile, Integer setday, String upurlk, String[] iparr) {
        Msg msg = new Msg();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        Config config = configMapper.getSourceype();
        UploadConfig uploadConfig = uploadConfigMapper.getUpdateConfig();
        String userip = GetIPS.getIpAddr(request);
        java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
        User u = (User) session.getAttribute("user");
        Integer usermemory = 0;
        Integer memory = 0;
        Integer sourcekey = 0;
        Integer maxsize = 0;
        String userpath = "tourist";
        String md5key = "";
        //FileInputStream fis = null;
        File file = SetFiles.changeFile_new(multipartFile);
        try {
            md5key = DigestUtils.md5Hex(new FileInputStream(file));
            if(!TypeDict.checkImgType(file)) {
                msg.setCode("4000");
                return msg;
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("5000");
            return msg;
        }
        //判断图片是否存在
        if(imgMapper.md5Count(md5key)>0){
            Print.warning("图片存在啦");
            Images images = imgMapper.selectImgUrlByMD5(md5key);
            jsonObject.put("imgurls", images.getImgurl());
            jsonObject.put("imgnames",file.getName());
            jsonArray.add(jsonObject);
            msg.setData(jsonArray);
            return msg;
        }
        String prefix = file.getName().substring(file.getName().lastIndexOf(".")+1);
        MultipartFile mf =null;
        try{
            InputStream inputStream = new FileInputStream(file);
             mf = new MockMultipartFile(file.getName(), inputStream);
            inputStream.close();
        }catch (Exception e){
            System.out.println("0000000");
        }

        if (uploadConfig.getBlacklist() != null) {
            iparr = uploadConfig.getBlacklist().split(";");
            for (String s : iparr) {
                if (s.equals(userip)) {
                    msg.setCode("911");
                    return msg;
                }
            }
        }
//        if (Integer.parseInt(Base64Encryption.decryptBASE64(upurlk)) != yzupdate()) {
        if (!upurlk.equals(UpdateImgController.vu)) {
            msg.setCode("4003");
            return msg;
        }
        //验证文件是否是图片
        if (u == null) {
            sourcekey = GetCurrentSource.GetSource(null);
            memory = uploadConfig.getVisitormemory();
            maxsize = uploadConfig.getFilesizetourists();
            usermemory = imgMapper.getusermemory(0);
            if (usermemory == null) {
                usermemory = 0;
            }
        } else {
            userpath = u.getUsername();
            sourcekey = GetCurrentSource.GetSource(u.getId());
            memory = userMapper.getUsers(u.getEmail()).getMemory();
            maxsize = uploadConfig.getFilesizeuser();
            usermemory = imgMapper.getusermemory(u.getId());
            if (usermemory == null) {
                usermemory = 0;
            }
        }
        if (uploadConfig.getUrltype() == 2) {
            userpath = dateFormat.format(new Date());
        }
        Keys key = keysMapper.selectKeys(sourcekey);
        int tmp = (memory == -1 ? -2 : (usermemory / 1024));
        if (tmp >= memory) {
            msg.setCode("4005");
            return msg;
        }
        long stime = System.currentTimeMillis();
        Map<String, MultipartFile> map = new HashMap<>();
        if (mf.getSize() > maxsize * 1024 * 1024) {
            msg.setCode("4006");
            return msg;
        }
        String fileName = mf.getOriginalFilename();
        if (!mf.isEmpty()) {
            map.put(prefix, mf);//prefix
        }
        Map<ReturnImage, Integer> m = null;
        m = GetSource.storageSource(key.getStorageType(), map, userpath, null, setday);
        Images img = new Images();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for (Map.Entry<ReturnImage, Integer> entry : m.entrySet()) {
            if(entry.getKey()!=null){
                if (key.getStorageType() == 5) {
                    if (config.getDomain() != null) {
                        jsonObject.put("imgurls", config.getDomain()  +"/"+ entry.getKey().getImgurl());
                        jsonObject.put("imgnames", entry.getKey().getImgname());
                        img.setImgurl(config.getDomain()  +"/"+ entry.getKey().getImgurl());
                    } else {
                        jsonObject.put("imgurls", config.getDomain()  +"/"+ entry.getKey().getImgurl());
                        jsonObject.put("imgnames", entry.getKey().getImgname());
                        img.setImgurl("http://" + IPPortUtil.getLocalIP() + ":" + IPPortUtil.getLocalPort()  +"/"+ entry.getKey().getImgurl());//图片链接
                    }
                } else {
                    jsonObject.put("imgurls", entry.getKey().getImgurl());
                    jsonObject.put("imgnames", entry.getKey().getImgname());
                    img.setImgurl( entry.getKey().getImgurl());
                }
                jsonArray.add(jsonObject);
                img.setUpdatetime(df.format(new Date()));
                img.setSource(key.getStorageType());
                img.setUserid(u == null ? 0 : u.getId());
                img.setSizes((entry.getValue()) / 1024);
                if(uploadConfig.getUrltype()==2){
                    //img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), "", ""));
                    String[] imgname = entry.getKey().getImgurl().split ("/");
                    String name = "";
                    for (int i = 0; i < imgname.length; i++) {
                        if(i>2 && i!=imgname.length-1){
                            name+=imgname[i]+"/";
                        }
                        if(i==imgname.length-1){
                            name+=imgname[i];
                        }
                    }
                    img.setImgname(entry.getKey().getImgname());
                }else{
                    img.setImgname(SetText.getSubString(entry.getKey().getImgurl(), key.getRequestAddress() + "/", ""));
                }
                img.setImgtype(setday > 0 ? 1 : 0);
                img.setAbnormal(userip);
                img.setMd5key(md5key);
                userMapper.insertimg(img);
                long etime = System.currentTimeMillis();
                Print.Normal("上传图片所用时长：" + String.valueOf(etime - stime) + "ms");
            }else{
                msg.setCode("5001");
                return msg;
            }
        }
        msg.setData(jsonArray);
        return msg;
    }

    public String uploadForURL() {
        return "";
    }

    private Integer yzupdate() {
        Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DATE);
        //int h=cal.get(Calendar.HOUR_OF_DAY);
        //int mm=cal.get(Calendar.MINUTE);
        return y + m + d + 999;
    }
}
