package cn.hellohao.service.impl;

import cn.hellohao.config.GlobalConstant;
import cn.hellohao.dao.KeysMapper;
import cn.hellohao.dao.UploadConfigMapper;
import cn.hellohao.dao.UserMapper;
import cn.hellohao.pojo.*;
import cn.hellohao.service.ConfdataService;
import cn.hellohao.service.ImgTempService;
import cn.hellohao.service.SysConfigService;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020/1/9 15:46
 */
@Service
public class UploadServicel {
    private static Logger logger = LoggerFactory.getLogger(UploadServicel.class);
    @Autowired
    ConfdataService confdataService;
    @Autowired
    SysConfigService sysConfigService;
    @Autowired private UploadConfigMapper uploadConfigMapper;
    @Autowired private KeysMapper keysMapper;
    @Autowired private ImgServiceImpl imgServiceImpl;
    @Autowired private UserMapper userMapper;
    @Autowired private ImgTempService imgTempService;
    @Autowired private GetSource getSource;
    @Autowired deleImages deleimages;
    @Autowired ImgViolationJudgeServiceImpl imgViolationJudgeService;

    public Msg uploadForLoc(
            HttpServletRequest request,
            File file,
            String imgName,
            Integer setday,
            JSONObject imgJson,
            String md5key) {
        Msg msg = new Msg();
        try {
            Confdata dataJson = confdataService.selectConfdata("config");
            JSONObject confdata = JSONObject.parseObject(dataJson.getJsondata());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject jsonObject = new JSONObject();
            UploadConfig uploadConfig = uploadConfigMapper.getUpdateConfig();
            String userip = GetIPS.getIpAddr(request);
            Subject subject = SecurityUtils.getSubject();
            User u = (User) subject.getPrincipal();
            if (null != u) {
                u = userMapper.getUsers(u);
            }
            Integer sourceKeyId = 0;
            if (imgJson != null) {
                Msg imgData = uploadForURL(request, imgJson.getString("imgUrl"),imgJson.getString("referer"));
                if (imgData.getCode().equals("200")) {
                    file = new File((String) imgData.getData());
                } else {
                    return imgData;
                }
            }
            FileInputStream stream = new FileInputStream(file);
            String imguid = UUID.randomUUID().toString().replace("-", "");
            Msg msg1 = updateImgCheck(u, uploadConfig);
            if (!msg1.getCode().equals("300")) {
                try {
                    if(stream!=null){stream.close();}
                    file.delete();
                } catch (Exception e) { }
                return msg1;
            }
            JSONObject updateJson = JSONObject.parseObject(msg1.getData().toString());
            final JSONObject groupJson = updateJson.getJSONObject("group");
            final Group group = groupJson.toJavaObject(Group.class);
            Long memory = updateJson.getLong("memory");
            Long UsedTotleMemory = updateJson.getLong("UsedTotleMemory");
            Long TotleMemory = updateJson.getLong("TotleMemory");
            String updatePath = updateJson.getString("updatePath");
            if (uploadConfig.getBlacklist() != null) {
                String[] iparr = uploadConfig.getBlacklist().split(";");
                for (String s : iparr) {
                    if (s.equals(userip)) {
                        file.delete();
                        msg.setCode("4003");
                        msg.setInfo("你暂时不能上传");
                        try {
                            if(stream!=null){stream.close();}
                            file.delete();
                        } catch (Exception e) { }
                        return msg;
                    }
                }
            }
            sourceKeyId = group.getKeyid();
            Keys key = keysMapper.selectKeys(sourceKeyId);
            Long tmp = (memory == -1 ? -2 : UsedTotleMemory);
            if (tmp >= memory) {
                msg.setCode("4005");
                msg.setInfo(u == null ? "游客空间已用尽" : "您的可用空间不足");
                try {
                    if(stream!=null){stream.close();}
                    file.delete();
                } catch (Exception e) { }
                return msg;
            }
            if (file.length() > TotleMemory) {
                msg.setCode("4006");
                msg.setInfo("图像超出系统限制大小");
                try {
                    if(stream!=null){stream.close();}
                    file.delete();
                } catch (Exception e) { }
                return msg;
            }
            Msg fileMiME = TypeDict.FileMiME(stream);
            if (!fileMiME.getCode().equals("200")) {
                msg.setCode("4000");
                msg.setInfo(fileMiME.getInfo());
                try {
                    if(stream!=null){stream.close();}
                    file.delete();
                } catch (Exception e) { }
                return msg;
            }
            if (md5key == null || md5key.equals("")) {
                md5key = DigestUtils.md5Hex(stream);
//                FileInputStream fis = null;
//                try {
//                    fis = new FileInputStream(file);
//                    md5key = DigestUtils.md5Hex(fis);
//                } catch (Exception e) {
//                    logger.error("获取MD5错误",e);
//                }finally{
//                    try {
//                        if (null != fis)
//                            fis.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
            String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            try {
                if(stream!=null){stream.close();}
            } catch (Exception e) { }
            // 判断路径格式 处理一下，去掉最前面的/
            String RootPath = key.getRootPath().replace(" ", "");
            if (!RootPath.equals("/")) {
                updatePath = RootPath.substring(1) + "/" + updatePath;
            }
            // 先存数据库
            Images imgObj = new Images();
            String imgnameEd = null;
            Map<Map<String, String>, File> map = new HashMap<>();
            String shortUuid_y = SetText.getShortUuid();
            String shortUuid_s = SetText.getShortUuid();
            if (file.exists()) {
                Map<String, String> m1 = new HashMap<>();
                m1.put("prefix", prefix);
                m1.put("name", shortUuid_y);
                map.put(m1, file);
                imgnameEd = updatePath + "/" + shortUuid_y + "." + prefix;
                imgObj.setImgname(imgnameEd);
                if (key.getStorageType().equals(5)) {
                    imgObj.setImgurl(confdata.getString("domain") + "/ota/" + imgnameEd);
                } else if(key.getStorageType().equals(9) && key.getSysTransmit()){
                    imgObj.setImgurl(confdata.getString("domain") + "/w/" + shortUuid_y);
                }else {
                    imgObj.setImgurl(key.getRequestAddress() + "/" + imgnameEd);
                }
                imgObj.setSizes(Long.toString(file.length()));
            }else{
                msg.setInfo("未获取到指定图像:110503");
                msg.setCode("110503");
                try { file.delete(); } catch (Exception e) { }
                return msg;
            }
            imgObj.setUpdatetime(df.format(new Date()));
            imgObj.setSource(key.getId());
            imgObj.setUserid(u == null ? 0 : u.getId());
            if (setday == 1 || setday == 3 || setday == 7 || setday == 30) {
                imgObj.setImgtype(1);
                ImgTemp imgDataExp = new ImgTemp();
                imgDataExp.setDeltime(plusDay(setday));
                imgDataExp.setImguid(imguid);
                imgTempService.insertImgExp(imgDataExp);
            } else {
                imgObj.setImgtype(0);
            }
            imgObj.setAbnormal(userip);
            imgObj.setMd5key(md5key);
            imgObj.setImguid(imguid);
            imgObj.setFormat(fileMiME.getData().toString());
            imgObj.setShortlink(shortUuid_y);
            imgObj.setBrieflink(shortUuid_s);
            imgObj.setIdname(imgName);
//            Integer insertRet = imgMapper.insertImgData(imgObj);
            Msg insertRet = imgServiceImpl.insertImgDataForCheck(imgObj,u,confdata,file.getName());
            if (insertRet.getCode().equals("000")) {
                try { file.delete(); } catch (Exception e) { }
                return insertRet;
            }

            long stime = System.currentTimeMillis();
            System.out.println("上传前地址："+updatePath);
            ReturnImage returnImage = getSource.storageSource(key.getStorageType(), map, updatePath, key.getId());
            if (returnImage.getCode().equals("200")) {
                long etime = System.currentTimeMillis();
                Print.Normal("上传图片所用总时长：" + String.valueOf(etime - stime) + "ms");
                jsonObject.put("url", imgObj.getImgurl());
                jsonObject.put("name", imgObj.getImgname());
                jsonObject.put("imguid", imgObj.getImguid());
                imgViolationJudgeService.LegalImageCheck(imgObj,key);
            } else {
                imgServiceImpl.deleimgForImgUid(imgObj.getImguid());
                msg.setCode("5001");
                msg.setInfo("上传服务内部错误");
                try { file.delete(); } catch (Exception e) { }
                return msg;
            }
            msg.setData(jsonObject);
            file.delete();
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            msg.setInfo("上传时发生了一些错误");
            msg.setCode("110500");
            file.delete();
            return msg;
        }
    }

    public CompletableFuture<Msg> mergeFile(HttpServletRequest request, String data, User user){
        Msg msg = new Msg();
        try{
            JSONObject jsonObj = JSONObject.parseObject(data);
            String filename = jsonObj.getString("filename");
            String uuid = jsonObj.getString("uuid");
            String md5 = jsonObj.getString("md5");
            String classifications = jsonObj.getString("classifications");
            Integer day = jsonObj.getInteger("day");
            String imgIdentifier = jsonObj.getString("imgIdentifier");
            JSONArray jsonArray = new JSONArray();
            classifications = classifications==null?"":classifications;
            if(!org.apache.commons.lang3.StringUtils.isBlank(classifications)){
                String[] calssif = classifications.split(",");
                for (int i = 0; i < calssif.length; i++) {
                    jsonArray.add(calssif[i]);
                }
            }
            String path = GlobalConstant.HELLOHAOTEMPIMG_PATH+File.separator+uuid+File.separator+imgIdentifier+File.separator+filename;
//            if(new File(path).exists()){
//                path = GlobalConstant.HELLOHAOTEMPIMG_PATH+File.separator+UUID.randomUUID().toString().replace("-", "").toLowerCase()+filename;
//            }
            String folder = GlobalConstant.HELLOHAOTEMPIMG_PATH+File.separator+uuid+File.separator+imgIdentifier;
            String delFolder = GlobalConstant.HELLOHAOTEMPIMG_PATH+File.separator+uuid;
            String originalFilename = filename;
            if(StringUtils.isBlank(originalFilename)){
                originalFilename = "未命名图像";
            }
            Msg merge = SetFiles.merge(path, folder, filename);
            if(merge.getCode().equals("200")){
                File file = new File(path);
//                CompletableFuture<Msg> ret =
                msg = uploadForLoc(request, file,originalFilename, day, null,md5);
//                msg = ret.get();
            }else{
                msg = merge;
            }
            SetFiles.delFileFolder(new File(delFolder));
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("500");
            msg.setInfo("文件上传时发生错误");
        }
        return CompletableFuture.completedFuture(msg);
    }


    public static Msg uploadForURL(HttpServletRequest request, String imgurls,String referer) {
        final Msg msg = new Msg();
        String url = imgurls;
        // 先判断是不是有效链接  并且自动判断协议头http(s)://
        String protocol = NetWorkTools.getProtocol(url,referer);
        if (protocol == null) {
            msg.setInfo("服务器解析该链接失败");
            msg.setCode("500");
            return msg;
        } else {
            url = protocol;
        }
        Long imgsize = 0L;
        try {
            if (imgsize == 0) {
                String ShortUID = SetText.getShortUuid();
                String savePath =
                        request.getSession().getServletContext().getRealPath("/")
                                + File.separator
                                + "hellohaotmp"
                                + File.separator;
                Map<String, Object> bl = ImgUrlUtil.downLoadFromUrl(url,referer, ShortUID, savePath);
                if ((Boolean) bl.get("res") == true) {
                    //                        File file = new File();
                    msg.setCode("200");
                    msg.setData(bl.get("imgPath")); // savePath + File.separator + ShortUID
                    return msg;
                } else {
                    if (bl.get("StatusCode").equals("110403")) {
                        msg.setInfo("该链接非图像文件，无法上传");
                    } else {
                        msg.setInfo("该链接暂时无法上传");
                    }
                    msg.setCode("500");
                }
            } else {
                msg.setCode("500");
                msg.setInfo("获取资源失败");
            }
        } catch (Exception e) {
            msg.setCode("500");
            msg.setInfo("获取资源失败");
        }

        return msg;
    }

//    private static Group group; // 上传用户或游客的所属分组
//    private static Long memory; // 上传用户或者游客的分配容量 memory
//    private static Long TotleMemory; // 用户或者游客下可使用的总容量 //maxsize
//    private static Long UsedTotleMemory; // 用户或者游客已经用掉的总容量 //usermemory
//    private static String updatePath = "tourist";

    // 判断用户 或 游客 当前上传图片的一系列校验
    private Msg updateImgCheck(User user, UploadConfig uploadConfig) {
        Msg msg = new Msg();
        JSONObject jsonObject = new JSONObject();
        String updatePath = "tourist";
        try {
            Group group = GetCurrentSource.GetSource(user == null ? null : user.getId());
            jsonObject.put("group",group);
            if (user == null) {
                if (uploadConfig.getIsupdate() != 1) {
                    msg.setCode("1000");
                    msg.setInfo("系统已禁用游客上传");
                    return msg;
                }
                updatePath = "tourist";
                Long memory = Long.valueOf(uploadConfig.getVisitormemory()); // 单位 B 游客设置总量
                Long TotleMemory = Long.valueOf(uploadConfig.getFilesizetourists()); // 单位 B  游客单文件大小
                Long UsedTotleMemory =
                        imgServiceImpl.getusermemory(0) == null
                                ? 0L
                                : imgServiceImpl.getusermemory(0); // 单位 B
                jsonObject.put("memory",memory);
                jsonObject.put("TotleMemory",TotleMemory);
                jsonObject.put("UsedTotleMemory",UsedTotleMemory);
            } else {
                if (uploadConfig.getUserclose() != 1) {
                    msg.setCode("1001");
                    msg.setInfo("系统已禁用上传功能");
                    return msg;
                }
                updatePath = user.getUsername();
                Long memory = Long.valueOf(user.getMemory()); // 单位 B  *1024*1024
                Long TotleMemory = Long.valueOf(uploadConfig.getFilesizeuser()); // 单位 B
                Long UsedTotleMemory =
                        imgServiceImpl.getusermemory(user.getId()) == null
                                ? 0L
                                : imgServiceImpl.getusermemory(user.getId()); // 单位 B
                jsonObject.put("memory",memory);
                jsonObject.put("TotleMemory",TotleMemory);
                jsonObject.put("UsedTotleMemory",UsedTotleMemory);
            }
            if (uploadConfig.getUrltype() == 2) {
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                updatePath = currentDate.format(formatter);
            }
            jsonObject.put("updatePath",updatePath);
            msg.setData(jsonObject);
            msg.setCode("300");
        } catch (Exception e) {
            logger.error("updateImgCheck异常：",e);
            msg.setCode("500");
        }
        return msg;
    }


    // 计算时间
    public static String plusDay(int setday) {
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        ca.add(Calendar.DATE, setday); // num为增加的天数，可以改变的
        d = ca.getTime();
        String enddate = format.format(d);
        System.out.println("到期的日期：" + enddate);
        return enddate;
    }
}
