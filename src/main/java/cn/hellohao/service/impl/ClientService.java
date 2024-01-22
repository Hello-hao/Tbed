package cn.hellohao.service.impl;

import cn.hellohao.dao.*;
import cn.hellohao.pojo.*;
import cn.hellohao.service.ConfdataService;
import cn.hellohao.service.ImgTempService;
import cn.hellohao.service.SysConfigService;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date 2021/10/28 16:38
 */
@Service
public class ClientService {
    private static Logger logger = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    ConfdataService confdataService;
    @Autowired
    SysConfigService sysConfigService;
    @Autowired private UserMapper userMapper;
    @Autowired private KeysMapper keysMapper;
    @Autowired private UploadConfigMapper uploadConfigMapper;
    @Autowired private ImgServiceImpl imgServiceImpl;
    @Autowired private ImgTempService imgTempService;
    @Autowired private GetSource getSource;
    @Autowired private ImgViolationJudgeServiceImpl imgViolationJudgeService;

    public Msg uploadImg(
            HttpServletRequest request, MultipartFile multipartFile, String email, String pass,Integer setday) {
        Msg msg = new Msg();
        try {
            Confdata dataJson = confdataService.selectConfdata("config");
            JSONObject confdata = JSONObject.parseObject(dataJson.getJsondata());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String imageFileName = "未命名图像";
            Integer sourceKeyId = 0;
            String md5key = null;
            JSONObject jsonObject = new JSONObject();
            String userip = GetIPS.getIpAddr(request);
            UploadConfig uploadConfig = uploadConfigMapper.getUpdateConfig();
            if (uploadConfig.getApi() != 1) {
                msg.setCode("4003");
                msg.setInfo("管理员关闭了API接口");
                return msg;
            }
            imageFileName = multipartFile.getOriginalFilename();
            File file = SetFiles.changeFile_new(multipartFile);
            User u2 = new User();
            if (!file.exists() || email == null || pass == null) {
                msg.setCode("4005");
                msg.setInfo("必要参数不能为空");
                return msg;
            }
            u2.setEmail(email);
            u2.setPassword(Base64Encryption.encryptBASE64(pass.getBytes()));
            User u = userMapper.getUsers(u2);
            if (null == u || u.getIsok() != 1) {
                msg.setCode("4006");
                msg.setInfo("用户信息不正确,账号异常");
                return msg;
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
                msg.setCode("4007");
                msg.setInfo(u == null ? "游客空间已用尽" : "您的可用空间不足");
                return msg;
            }
            if (file.length() > TotleMemory) {
                System.err.println("文件大小：" + file.length());
                System.err.println("最大限制：" + TotleMemory);
                msg.setCode("4008");
                msg.setInfo("图像超出系统限制大小");
                return msg;
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                md5key = DigestUtils.md5Hex(fis);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("未获取到图片的MD5,成成UUID");
            }finally{
                try {
                    if (null != fis)
                        fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Msg fileMiME = TypeDict.FileMiME(file);
            if (!fileMiME.getCode().equals("200")) {
                msg.setCode("4009");
                msg.setInfo(fileMiME.getInfo());
                return msg;
            }
            if (md5key == null || md5key.equals("")) {
                md5key = UUID.randomUUID().toString().replace("-", "");
            }
            String prefix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            if (uploadConfig.getBlacklist() != null) {
                String[] iparr = uploadConfig.getBlacklist().split(";");
                for (String s : iparr) {
                    if (s.equals(userip)) {
                        msg.setCode("4010");
                        msg.setInfo("你暂时不能上传");
                        return msg;
                    }
                }
            }
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
                imgDataExp.setDeltime(UploadServicel.plusDay(setday));
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
            imgObj.setIdname(imageFileName);
            Msg insertRet = imgServiceImpl.insertImgDataForCheck(imgObj,u,confdata,file.getName());
            if (insertRet.getCode().equals("000")) {
                return insertRet;
            }
            long stime = System.currentTimeMillis();
            ReturnImage returnImage = getSource.storageSource(key.getStorageType(), map, updatePath, key.getId());
            if (returnImage.getCode().equals("200")) {
                long etime = System.currentTimeMillis();
                logger.info("上传图片所用总时长：" + String.valueOf(etime - stime) + "ms");
                jsonObject.put("url", imgObj.getImgurl());
                jsonObject.put("name", imgObj.getImgname());
                jsonObject.put("size", imgObj.getSizes());
                imgViolationJudgeService.LegalImageCheck(imgObj,key);
            } else {
                imgServiceImpl.deleimgForImgUid(imgObj.getImguid());
                msg.setCode("5001");
                msg.setInfo("上传服务内部错误");
                return msg;
            }
            file.delete();
            msg.setData(jsonObject);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("5001");
            msg.setInfo("Error for server:500");
            return msg;
        }
    }

    public static Group group;
    public static Long memory;
    public static Long TotleMemory;
    public static Long UsedTotleMemory;
    public static String updatePath = "tourist";

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
                group = GetCurrentSource.GetSource(null);
                memory = Long.valueOf(uploadConfig.getVisitormemory()); // 单位 B 游客设置总量
                TotleMemory = Long.valueOf(uploadConfig.getFilesizetourists()); // 单位 B  游客单文件大小
                UsedTotleMemory =
                        imgServiceImpl.getusermemory(0) == null
                                ? 0L
                                : imgServiceImpl.getusermemory(0); // 单位 B
            } else {
                // 判断用户能不能上传
                if (uploadConfig.getUserclose() != 1) {
                    msg.setCode("1001");
                    msg.setInfo("系统已禁用上传功能");
                    return msg;
                }
                updatePath = user.getUsername();
                group = GetCurrentSource.GetSource(user.getId());
                memory = Long.valueOf(user.getMemory()); // 单位 B
                TotleMemory = Long.valueOf(uploadConfig.getFilesizeuser()); // 单位 B
                UsedTotleMemory =
                        imgServiceImpl.getusermemory(user.getId()) == null
                                ? 0L
                                : imgServiceImpl.getusermemory(user.getId()); // 单位 B
            }
            // 判断上传的图片目录结构类型
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

}
