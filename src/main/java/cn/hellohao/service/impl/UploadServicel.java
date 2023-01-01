package cn.hellohao.service.impl;

import cn.hellohao.dao.*;
import cn.hellohao.pojo.*;
import cn.hellohao.service.ImgTempService;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020/1/9 15:46
 */
@Service
public class UploadServicel {
    @Autowired private UploadConfigMapper uploadConfigMapper;
    @Autowired private KeysMapper keysMapper;
    @Autowired private ImgMapper imgMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private ImgreviewMapper imgreviewMapper;
    @Autowired private ImgTempService imgTempService;
    @Autowired private GetSource getSource;

    public Msg uploadForLoc(
            HttpServletRequest request,
            MultipartFile multipartFile,
            Integer setday,
            String imgUrl) {
        Msg msg = new Msg();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String imageFileName = "未命名图像";
            JSONObject jsonObject = new JSONObject();
            UploadConfig uploadConfig = uploadConfigMapper.getUpdateConfig();
            String userip = GetIPS.getIpAddr(request);
            Subject subject = SecurityUtils.getSubject();
            User u = (User) subject.getPrincipal();
            if (null != u) {
                u = userMapper.getUsers(u);
            }
            Integer sourceKeyId = 0;
            String md5key = null;
            FileInputStream fis = null;
            File file = null;
            if (imgUrl == null) {
                imageFileName = multipartFile.getOriginalFilename();
                file = SetFiles.changeFile_new(multipartFile);
            } else {
                imageFileName = "URL转存图像";
                Msg imgData = uploadForURL(request, imgUrl);
                if (imgData.getCode().equals("200")) {
                    file = new File((String) imgData.getData());
                } else {
                    return imgData;
                }
            }
            String imguid = UUID.randomUUID().toString().replace("-", "");
            Msg msg1 = updateImgCheck(u, uploadConfig);
            if (!msg1.getCode().equals("300")) {
                return msg1;
            }
            sourceKeyId = group.getKeyid();
            Keys key = keysMapper.selectKeys(sourceKeyId);
            Long tmp = (memory == -1 ? -2 : UsedTotleMemory);
            if (tmp >= memory) {
                msg.setCode("4005");
                msg.setInfo(u == null ? "游客空间已用尽" : "您的可用空间不足");
                return msg;
            }
            if (file.length() > TotleMemory) {
                System.err.println("文件大小：" + file.length());
                System.err.println("最大限制：" + TotleMemory);
                msg.setCode("4006");
                msg.setInfo("图像超出系统限制大小");
                return msg;
            }
            try {
                fis = new FileInputStream(file);
                md5key = DigestUtils.md5Hex(fis);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("未获取到图片的MD5,成成UUID");
            }
            Msg fileMiME = TypeDict.FileMiME(file);
            if (!fileMiME.getCode().equals("200")) {
                msg.setCode("4000");
                msg.setInfo(fileMiME.getInfo());
                return msg;
            }
            if (md5key == null || md5key.equals("")) {
                md5key = UUID.randomUUID().toString().replace("-", "");
            }
            String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            // 判断黑名单
            if (uploadConfig.getBlacklist() != null) {
                String[] iparr = uploadConfig.getBlacklist().split(";");
                for (String s : iparr) {
                    if (s.equals(userip)) {
                        file.delete();
                        msg.setCode("4003");
                        msg.setInfo("你暂时不能上传");
                        return msg;
                    }
                }
            }
            // 先存数据库
            Images imgObj = new Images();
            String imgnameEd = null;
            Map<Map<String, String>, File> map = new HashMap<>();
            if (file.exists()) {
                Map<String, String> m1 = new HashMap<>();
                String shortUuid_y = SetText.getShortUuid();
                m1.put("prefix", prefix);
                m1.put("name", shortUuid_y);
                map.put(m1, file);
                imgnameEd = updatePath + "/" + shortUuid_y + "." + prefix;
                imgObj.setImgname(imgnameEd);
                if (key.getStorageType().equals(5)) {
                    imgObj.setImgurl(key.getRequestAddress() + "/ota/" + imgnameEd);
                } else {
                    imgObj.setImgurl(key.getRequestAddress() + "/" + imgnameEd);
                }
                imgObj.setSizes(Long.toString(file.length()));
            }else{
                msg.setInfo("未获取到指定图像:110503");
                msg.setCode("110503");
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
            imgObj.setIdname(imageFileName);
            Integer insertRet = imgMapper.insertImgData(imgObj);
            if (insertRet == 0) {
                Images imaOBJ = new Images();
                imaOBJ.setMd5key(md5key);
                imaOBJ.setUserid(u == null ? 0 : u.getId());
                List<Images> images = imgMapper.selectImgUrlByMD5(md5key);
                if (images.size() > 0) {
                    jsonObject.put("url", images.get(0).getImgurl());
//                    Keys imgFromKey = keysService.selectKeys(images.get(0).getSource());
//                    if (imgFromKey.getStorageType().equals(5)) {
//                        jsonObject.put(
//                                "url",
//                                imgFromKey.getRequestAddress()
//                                        + "/ota/"
//                                        + images.get(0).getImgname());
//                    } else {
//                        jsonObject.put("url", images.get(0).getImgurl());
//                    }
                    jsonObject.put("name", file.getName());
                    jsonObject.put("imguid", images.get(0).getImguid());
                    msg.setData(jsonObject);
                    return msg;
                } else {
                    msg.setInfo("未获取到指定图像");
                    msg.setCode("110501");
                    return msg;
                }
            }
            long stime = System.currentTimeMillis();
            ReturnImage returnImage = getSource.storageSource(key.getStorageType(), map, updatePath, key.getId());
            if (returnImage.getCode().equals("200")) {
                long etime = System.currentTimeMillis();
                Print.Normal("上传图片所用总时长：" + String.valueOf(etime - stime) + "ms");
                jsonObject.put("url", imgObj.getImgurl());
                jsonObject.put("name", imgObj.getImgname());
                jsonObject.put("imguid", imgObj.getImguid());
                new Thread(
                                () -> {
                                    LegalImageCheck(imgObj);
                                })
                        .start();
            } else {
                msg.setCode("5001");
                msg.setInfo("上传服务内部错误");
                return msg;
            }
            file.delete();
            msg.setData(jsonObject);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            msg.setInfo("上传时发生了一些错误");
            msg.setCode("110500");
            return msg;
        }
    }

    public static Msg uploadForURL(HttpServletRequest request, String imgurl) {
        Msg msg = new Msg();
        if (true) {
            Long imgsize = 0L;
            try {
                if (imgsize == 0) {
                    String ShortUID = SetText.getShortUuid();
                    String savePath =
                            request.getSession().getServletContext().getRealPath("/")
                                    + File.separator
                                    + "hellohaotmp"
                                    + File.separator;
                    Map<String, Object> bl = ImgUrlUtil.downLoadFromUrl(imgurl, ShortUID, savePath);
                    if ((Boolean) bl.get("res") == true) {
                        msg.setCode("200");
                        msg.setData(bl.get("imgPath"));
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
        } else {
            msg.setCode("500");
            msg.setInfo("该链接无效");
        }

        return msg;
    }

    private static Group group; // 上传用户或游客的所属分组
    private static Long memory; // 上传用户或者游客的分配容量 memory
    private static Long TotleMemory; // 用户或者游客下可使用的总容量 //maxsize
    private static Long UsedTotleMemory; // 用户或者游客已经用掉的总容量 //usermemory
    private static String updatePath = "tourist";

    // 判断用户 或 游客 当前上传图片的一系列校验
    private Msg updateImgCheck(User user, UploadConfig uploadConfig) {
        Msg msg = new Msg();
        java.text.DateFormat dateFormat = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            if (user == null) {
                if (uploadConfig.getIsupdate() != 1) {
                    msg.setCode("1000");
                    msg.setInfo("系统已禁用游客上传");
                    return msg;
                }
                updatePath = "tourist";
                group = GetCurrentSource.GetSource(null);
                memory = Long.valueOf(uploadConfig.getVisitormemory()); // 单位 B 游客设置总量
                TotleMemory = Long.valueOf(uploadConfig.getFilesizetourists()); // 单位 B  游客单文件大小
                UsedTotleMemory =
                        imgMapper.getusermemory(0) == null
                                ? 0L
                                : imgMapper.getusermemory(0); // 单位 B
            } else {
                if (uploadConfig.getUserclose() != 1) {
                    msg.setCode("1001");
                    msg.setInfo("系统已禁用上传功能");
                    return msg;
                }
                updatePath = user.getUsername();
                group = GetCurrentSource.GetSource(user.getId());
                memory = Long.valueOf(user.getMemory()); // 单位 B  *1024*1024
                TotleMemory = Long.valueOf(uploadConfig.getFilesizeuser()); // 单位 B
                UsedTotleMemory =
                        imgMapper.getusermemory(user.getId()) == null
                                ? 0L
                                : imgMapper.getusermemory(user.getId()); // 单位 B
            }
            if (uploadConfig.getUrltype() == 2) {
                updatePath = dateFormat.format(new Date());
            }
            msg.setCode("300");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("500");
        }
        return msg;
    }

    private synchronized void LegalImageCheck(Images images) {
        System.out.println("非法图像鉴别进程启动");
        Imgreview imgreview = null;
        try {
            imgreview = imgreviewMapper.selectByusing(1);
        } catch (Exception e) {
            Print.warning("获取鉴别程序的时候发生错误");
            e.printStackTrace();
        }
        if (null != imgreview) {
            LegalImageCheckForBaiDu(imgreview, images);
        }
    }

    private void LegalImageCheckForBaiDu(Imgreview imgreview, Images images) {
        System.out.println("非法图像鉴别进程启动-BaiDu");
        if (imgreview.getUsing() == 1) {
            try {
                AipContentCensor client =
                        new AipContentCensor(
                                imgreview.getAppId(),
                                imgreview.getApiKey(),
                                imgreview.getSecretKey());
                client.setConnectionTimeoutInMillis(5000);
                client.setSocketTimeoutInMillis(30000);
                org.json.JSONObject res = client.antiPorn(images.getImgurl());
                res = client.imageCensorUserDefined(images.getImgurl(), EImgType.URL, null);
                System.err.println("返回的鉴黄json:" + res.toString());
                com.alibaba.fastjson.JSONArray jsonArray =
                        JSON.parseArray("[" + res.toString() + "]");
                for (Object o : jsonArray) {
                    com.alibaba.fastjson.JSONObject jsonObject =
                            (com.alibaba.fastjson.JSONObject) o;
                    com.alibaba.fastjson.JSONArray data = jsonObject.getJSONArray("data");
                    Integer conclusionType = jsonObject.getInteger("conclusionType");
                    if (conclusionType != null) {
                        if (conclusionType == 2) {
                            for (Object datum : data) {
                                com.alibaba.fastjson.JSONObject imgdata =
                                        (com.alibaba.fastjson.JSONObject) datum;
                                if (imgdata.getInteger("type") == 1) {
                                    Images img = new Images();
                                    img.setImgname(images.getImgname());
                                    img.setViolation("1[1]");
                                    imgMapper.setImg(img);
                                    Imgreview imgv = new Imgreview();
                                    imgv.setId(1);
                                    Integer count = imgreview.getCount();
                                    System.out.println("违法图片总数：" + count);
                                    imgv.setCount(count + 1);
                                    imgreviewMapper.updateByPrimaryKeySelective(imgv);
                                    System.err.println("存在非法图片，进行处理操作");
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("图像鉴黄线程执行过程中出现异常");
                e.printStackTrace();
            }
        }
    }

    // 计算时间
    public static String plusDay(int setday) {
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currdate = format.format(d);
        System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        ca.add(Calendar.DATE, setday); // num为增加的天数，可以改变的
        d = ca.getTime();
        String enddate = format.format(d);
        System.out.println("到期的日期：" + enddate);
        return enddate;
    }
}
