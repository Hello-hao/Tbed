package cn.hellohao.service.impl;

import cn.hellohao.dao.*;
import cn.hellohao.pojo.*;
import cn.hellohao.service.ImgTempService;
import cn.hellohao.service.SysConfigService;
import cn.hellohao.utils.*;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
    SysConfigService sysConfigService;
    @Autowired
    UploadConfigMapper uploadConfigMapper;
    @Autowired
    KeysMapper keysMapper;
    @Autowired
    ImgMapper imgMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ImgreviewMapper imgreviewMapper;
    @Autowired
    ImgTempService imgTempService;



    public Msg uploadForLoc(HttpServletRequest request,
                            MultipartFile multipartFile, Integer setday, String imgUrl, JSONArray selectTreeList) {
        Msg msg = new Msg();
        try{
            JSONObject jsonObject = new JSONObject();
            UploadConfig uploadConfig = uploadConfigMapper.getUpdateConfig();
            String userip = GetIPS.getIpAddr(request);
            Subject subject = SecurityUtils.getSubject();
            User u = (User) subject.getPrincipal();
            if(null!=u){
                u =  userMapper.getUsers(u);
            }
            Integer sourceKeyId = 0;
            String md5key = null;
            FileInputStream fis = null;
            File file =null;
            if(imgUrl==null){
                file = SetFiles.changeFile_new(multipartFile);
            }else{
                //说明是URL上传
                Msg imgData = uploadForURL(request, imgUrl);
                if(imgData.getCode().equals("200")){
                    file = new File((String) imgData.getData());
                }else{
                    return imgData;
                }
            }
            String imguid = UUID.randomUUID().toString().replace("-", "");
            //判断上传前的一些用户限制信息
            Msg msg1 = updateImgCheck(u,uploadConfig);
            if(!msg1.getCode().equals("300")){
                return msg1;
            }

            //判断可用容量
            sourceKeyId = group.getKeyid();
            Keys key = keysMapper.selectKeys(sourceKeyId);
            Long tmp = (memory == -1 ? -2 : UsedTotleMemory);
            if (tmp >= memory) {
                msg.setCode("4005");
                msg.setInfo(u==null?"游客空间已用尽":"您的可用空间不足");
                return msg;
            }

            //判断图片有没有超出设定大小
            if (file.length() > TotleMemory) {
                System.err.println("文件大小："+file.length());
                System.err.println("最大限制："+TotleMemory);
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
            if(!fileMiME.getCode().equals("200")) {
                //非图像文本
                msg.setCode("4000");
                msg.setInfo(fileMiME.getInfo());
                return msg;
            }
            if(md5key==null || md5key.equals("")){
                md5key = UUID.randomUUID().toString().replace("-", "");
            }

            //判断图片是否存在
            if(Integer.valueOf(sysConfigService.getstate().getCheckduplicate())==1){
                Images imaOBJ = new Images();
                imaOBJ.setMd5key(md5key);
                imaOBJ.setUserid(u==null?0:u.getId());
                if(imgMapper.md5Count(imaOBJ)>0){
                    Images images = imgMapper.selectImgUrlByMD5(md5key);
                    jsonObject.put("url", images.getImgurl());
                    jsonObject.put("name",file.getName());
                    jsonObject.put("imguid",images.getImguid());
//                    jsonObject.put("shortLink",images.getShortlink());
                    msg.setData(jsonObject);
                    return msg;
                }
            }


            String prefix = file.getName().substring(file.getName().lastIndexOf(".")+1);
            //判断黑名单
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

            Map<String, File> map = new HashMap<>();
            if (file.exists()) {
                map.put(prefix, file);//prefix
            }
            long stime = System.currentTimeMillis();
            Map<ReturnImage, Integer> m = null;
            ReturnImage returnImage = GetSource.storageSource(key.getStorageType(), map, updatePath, key.getId());
            Images img = new Images();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(returnImage.getCode().equals("200")){
                String imgurl = returnImage.getImgurl();
                Long imgsize = returnImage.getImgSize();
                String imgname = returnImage.getImgname();
                img.setImgurl(imgurl);
                img.setUpdatetime(df.format(new Date()));
                img.setSource(key.getId());
                img.setUserid(u == null ? 0 : u.getId());
                img.setSizes(imgsize.toString());
                if(uploadConfig.getUrltype()==2){
                    img.setImgname(imgname);
                }else{
                    img.setImgname(SetText.getSubString(imgname, key.getRequestAddress() + "/", ""));
                }
                if(setday == 1 || setday == 3 || setday == 7 || setday == 30){
                    img.setImgtype(1);
                    ImgTemp imgDataExp = new ImgTemp();
                    imgDataExp.setDeltime(plusDay(setday));
                    imgDataExp.setImguid(imguid);
                    imgTempService.insertImgExp(imgDataExp);
                }else{
                    img.setImgtype(0);
                }
                img.setAbnormal(userip);
                img.setMd5key(md5key);
                img.setImguid(imguid);
                img.setFormat(fileMiME.getData().toString());
                userMapper.insertimg(img);
                long etime = System.currentTimeMillis();
                Print.Normal("上传图片所用总时长：" + String.valueOf(etime - stime) + "ms");
                jsonObject.put("url", img.getImgurl());
                jsonObject.put("name", imgname);
                jsonObject.put("imguid",img.getImguid());
//                jsonObject.put("shortLink", img.getShortlink());
                new Thread(()->{LegalImageCheck(img);}).start();
            }else{
                msg.setCode("5001");
                msg.setInfo("上传服务内部错误");
                return msg;
            }
            file.delete();
            msg.setData(jsonObject);
            return msg;
        }catch (Exception e){
            e.printStackTrace();
            msg.setInfo("上传时发生了一些错误");
            msg.setCode("110500");
            return msg;
        }

    }


    //通过图片Url上传图片
    public static Msg uploadForURL(HttpServletRequest request, String imgurl){
        final Msg msg = new Msg();
        //先判断是不是有效链接
//        final boolean valid = ImgUrlUtil.isValid(imgurl);
        if(true){
            Long imgsize = null;
            try {
                imgsize = ImgUrlUtil.getFileLength(imgurl);
                if(imgsize>0){
//                    String uuid= UUID.randomUUID().toString().replace("-", "");
                    String ShortUID = SetText.getShortUuid();
                    String savePath = request.getSession().getServletContext().getRealPath("/")+File.separator+"hellohaotmp"+File.separator;
                    Map<String ,Object> bl =ImgUrlUtil.downLoadFromUrl (imgurl, ShortUID, savePath);
                    if((Boolean) bl.get("res")==true){
//                        File file = new File();
                        msg.setCode("200");
                        msg.setData(bl.get("imgPath"));//savePath + File.separator + ShortUID
                        return msg;
                    }else{
                        if(bl.get("StatusCode").equals("110403")){
                            msg.setInfo("该链接非图像文件，无法上传");
                        }else{
                            msg.setInfo("该链接暂时无法上传");
                        }
                        msg.setCode("500");
                    }
                }else{
                    msg.setCode("500");
                    msg.setInfo("获取资源失败");
                }
            } catch (IOException e) {
                msg.setCode("500");
                msg.setInfo("获取资源失败");
            }
        }else{
            msg.setCode("500");
            msg.setInfo("该链接无效");
        }
        return msg;
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

    private synchronized void LegalImageCheck(Images images){
        System.out.println("非法图像鉴别进程启动");
        Imgreview imgreview = null;
        try {
            imgreview = imgreviewMapper.selectByusing(1);
        } catch (Exception e) {
            Print.warning("获取鉴别程序的时候发生错误");
            e.printStackTrace();
        }
        if(null != imgreview){
            LegalImageCheckForBaiDu(imgreview,images);
        }

    }

    private void LegalImageCheckForBaiDu(Imgreview imgreview,Images images){
        System.out.println("非法图像鉴别进程启动-BaiDu");
        if(imgreview.getUsing()==1){
            try {
                AipContentCensor client = new AipContentCensor(imgreview.getAppId(), imgreview.getApiKey(), imgreview.getSecretKey());
                client.setConnectionTimeoutInMillis(5000);
                client.setSocketTimeoutInMillis(30000);
                org.json.JSONObject res = client.antiPorn(images.getImgurl());
                res = client.imageCensorUserDefined(images.getImgurl(), EImgType.URL, null);
                System.err.println("返回的鉴黄json:"+res.toString());
                com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray("[" + res.toString() + "]");
                for (Object o : jsonArray) {
                    com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) o;
                    com.alibaba.fastjson.JSONArray data = jsonObject.getJSONArray("data");
                    Integer conclusionType = jsonObject.getInteger("conclusionType");
                    if(conclusionType!=null){
                        if (conclusionType == 2) {
                            for (Object datum : data) {
                                com.alibaba.fastjson.JSONObject imgdata = (com.alibaba.fastjson.JSONObject) datum;
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

            }catch (Exception e){
                System.out.println("图像鉴黄线程执行过程中出现异常");
                e.printStackTrace();

            }

        }
    }


    //计算时间
    public static String plusDay(int setday){
        Date d = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currdate = format.format(d);
        System.out.println("现在的日期是：" + currdate);
        Calendar ca = Calendar.getInstance();
        ca.setTime(d);
        ca.add(Calendar.DATE, setday);// num为增加的天数，可以改变的
        d = ca.getTime();
        String enddate = format.format(d);
        System.out.println("到期的日期：" + enddate);
        return enddate;
    }


}
