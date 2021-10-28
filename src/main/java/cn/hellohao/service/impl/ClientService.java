package cn.hellohao.service.impl;

import cn.hellohao.dao.*;
import cn.hellohao.pojo.*;
import cn.hellohao.pojo.vo.PageResultBean;
import cn.hellohao.service.ImgAndAlbumService;
import cn.hellohao.service.SysConfigService;
import cn.hellohao.utils.*;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.coobird.thumbnailator.filters.Watermark;
import org.apache.commons.codec.digest.DigestUtils;
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

    @Autowired
    private ImgAndAlbumService imgAndAlbumService;
    @Autowired
    private NOSImageupload nOSImageupload;
    @Autowired
    private OSSImageupload ossImageupload;
    @Autowired
    private USSImageupload ussImageupload;
    @Autowired
    private KODOImageupload kodoImageupload;
    @Autowired
    private COSImageupload cosImageupload;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private FTPImageupload ftpImageupload;
    @Autowired
    private UFileImageupload uFileImageupload;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private KeysMapper keysMapper;
    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private UploadConfigMapper uploadConfigMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private ImgMapper imgMapper;
    @Autowired
    ImgreviewMapper imgreviewMapper;


    public Msg uploadImg(HttpServletRequest request, MultipartFile multipartFile, String email, String pass ){
        Msg msg = new Msg();
        try {
            Integer sourceKeyId = 0;
            FileInputStream fis = null;
            String md5key = null;
            Integer setday = 0;
            JSONObject jsonObject = new JSONObject();
            Config config = configMapper.getSourceype();
            String userip = GetIPS.getIpAddr(request);
            UploadConfig uploadConfig = uploadConfigMapper.getUpdateConfig();
            if (uploadConfig.getApi() != 1) {
                msg.setCode("4003");
                msg.setInfo("管理员关闭了API接口");
                return msg;
            }
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
            //判断用户的账号密码是否存在（正确）
            if (null == u || u.getIsok() != 1) {
                msg.setCode("4006");
                msg.setInfo("用户信息不正确,账号异常");
                return msg;
            }
            String imguid = UUID.randomUUID().toString().replace("-", "");
            //判断上传前的一些用户限制信息
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
            try {
                fis = new FileInputStream(file);
                md5key = DigestUtils.md5Hex(fis);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("未获取到图片的MD5,成成UUID");
            }
            Msg fileMiME = TypeDict.FileMiME(file, uploadConfig.getSuffix());
            if (!fileMiME.getCode().equals("200")) {
                //非图像文本
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
            //判断图片是否存在
            if(Integer.valueOf(sysConfigService.getstate().getCheckduplicate())==1) {
                Images imaOBJ = new Images();
                imaOBJ.setMd5key(md5key);
                imaOBJ.setUserid(u.getId());
                if (imgMapper.md5Count(imaOBJ) > 0) {
                    Images images = imgMapper.selectImgUrlByMD5(md5key);
                    jsonObject.put("url", images.getImgurl());
                    jsonObject.put("name", file.getName());
                    jsonObject.put("size", images.getSizes());
                    msg.setData(jsonObject);
                    return msg;
                }
            }
            Map<String, File> map = new HashMap<>();
            String fileName = file.getName();
            if (file.exists()) {
                map.put(prefix, file);
            }
            long stime = System.currentTimeMillis();
            Map<ReturnImage, Integer> m = null;
            ReturnImage returnImage = GetSource.storageSource(key.getStorageType(), map, updatePath,  key.getId());
            Images img = new Images();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (returnImage.getCode().equals("200")) {
                String imgurl = returnImage.getImgurl();
                Long imgsize = returnImage.getImgSize();
                String imgname = returnImage.getImgname();
                img.setImgurl(imgurl);
                img.setUpdatetime(df.format(new Date()));
                img.setSource(key.getId());
                img.setUserid(u == null ? 0 : u.getId());
                img.setSizes(imgsize.toString());
                if (uploadConfig.getUrltype() == 2) {
                    img.setImgname(imgname);
                } else {
                    img.setImgname(SetText.getSubString(imgname, key.getRequestAddress() + "/", ""));
                }
                img.setImgtype(setday > 0 ? 1 : 0);
                img.setAbnormal(userip);
                img.setMd5key(md5key);
                img.setImguid(imguid);
                img.setFormat(fileMiME.getData().toString());
                userMapper.insertimg(img);
                long etime = System.currentTimeMillis();
                Print.Normal("上传图片所用总时长：" + String.valueOf(etime - stime) + "ms");
                jsonObject.put("url", img.getImgurl());
                jsonObject.put("name", imgname);
                jsonObject.put("size", img.getSizes());
                //启动鉴黄线程
                new Thread(() -> {
                    LegalImageCheck(img);
                }).start();
            } else {
                msg.setCode("5001");
                msg.setInfo("上传服务内部错误");
                return msg;
            }
            file.delete();
            msg.setData(jsonObject);
//            新代码结束=========
            return msg;
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("5001");
            msg.setInfo("Error for server:500");
            return msg;
        }

    }


    public static Group group; //上传用户或游客的所属分组
    public static Long memory;//上传用户或者游客的分配容量 memory
    public static Long TotleMemory;//用户或者游客下可使用的总容量 //maxsize
    public static Long UsedTotleMemory;//用户或者游客已经用掉的总容量 //usermemory
    public static String updatePath="tourist";

    //判断用户 或 游客 当前上传图片的一系列校验
    private Msg updateImgCheck(User user, UploadConfig uploadConfig){
        final Msg msg = new Msg();
        java.text.DateFormat dateFormat = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            if (user == null) {
                //用户没有登陆，值判断游客能不能上传即可
                if(uploadConfig.getIsupdate()!=1){
                    msg.setCode("1000");
                    msg.setInfo("系统已禁用游客上传");
                    return msg;
                }
                group = GetCurrentSource.GetSource(null);
                memory = Long.valueOf(uploadConfig.getVisitormemory());//单位 B 游客设置总量
                TotleMemory = Long.valueOf(uploadConfig.getFilesizetourists());//单位 B  游客单文件大小
                UsedTotleMemory = imgMapper.getusermemory(0)==null?0L : imgMapper.getusermemory(0);//单位 B
            } else {
                //判断用户能不能上传
                if(uploadConfig.getUserclose()!=1){
                    msg.setCode("1001");
                    msg.setInfo("系统已禁用上传功能");
                    return msg;
                }
                updatePath = user.getUsername();
                group= GetCurrentSource.GetSource(user.getId());
                memory = Long.valueOf(user.getMemory())*1024*1024;//单位 B
                TotleMemory = Long.valueOf(uploadConfig.getFilesizeuser());//单位 B
                UsedTotleMemory = imgMapper.getusermemory(user.getId())==null?0L:imgMapper.getusermemory(user.getId());//单位 B
            }
            //判断上传的图片目录结构类型
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

    //图片鉴黄

    private synchronized void LegalImageCheck(Images images){
        System.out.println("非法图像鉴别进程启动");
        Imgreview imgreview = null;
        try {
            imgreview = imgreviewMapper.selectByusing(1);
        } catch (Exception e) {
            Print.warning("获取鉴别程序的时候发生错误");
            e.printStackTrace();
        }
        LegalImageCheckForBaiDu(imgreview,images);
    }

    private void LegalImageCheckForBaiDu(Imgreview imgreview,Images images){
        if(imgreview.getUsing()==1){
            try {
                AipContentCensor client = new AipContentCensor(imgreview.getAppId(), imgreview.getApiKey(), imgreview.getSecretKey());
                client.setConnectionTimeoutInMillis(5000);
                client.setSocketTimeoutInMillis(30000);
                org.json.JSONObject res = client.antiPorn(images.getImgurl());
                res = client.imageCensorUserDefined(images.getImgurl(), EImgType.URL, null);
                com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray("[" + res.toString() + "]");
                for (Object o : jsonArray) {
                    JSONObject jsonObject = (JSONObject) o;
                    com.alibaba.fastjson.JSONArray data = jsonObject.getJSONArray("data");
                    Integer conclusionType = jsonObject.getInteger("conclusionType");
                    if(conclusionType!=null){
                        if (conclusionType == 2) {
                            for (Object datum : data) {
                                JSONObject imgdata = (JSONObject) datum;
                                if (imgdata.getInteger("type") == 1) {
                                    Images img = new Images();
                                    img.setImgname(images.getImgname());
                                    img.setViolation("1[1]");//数字是鉴别平台的主键ID，括号是非法的类型，参考上面的注释
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

            }catch (Exception e){
                System.out.println("图像鉴黄线程执行过程中出现异常");
                e.printStackTrace();

            }

        }
    }


}
