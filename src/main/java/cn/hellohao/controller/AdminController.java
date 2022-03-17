package cn.hellohao.controller;

import cn.hellohao.config.SysName;
import cn.hellohao.pojo.*;
import cn.hellohao.pojo.vo.PageResultBean;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.*;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-07-17 14:22
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ImgService imgService;
    @Autowired
    private KeysService keysService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ImgreviewService imgreviewService;
    @Autowired
    private ImgTempService imgTempService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private ImgAndAlbumService imgAndAlbumService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    AlbumServiceImpl albumServiceI;

    @Autowired
    private NOSImageupload nosImageupload;
    @Autowired
    private OSSImageupload ossImageupload;
    @Autowired
    private COSImageupload cosImageupload;
    @Autowired
    private KODOImageupload kodoImageupload;
    @Autowired
    private USSImageupload ussImageupload;
    @Autowired
    private UFileImageupload uFileImageupload;
    @Autowired
    private FTPImageupload ftpImageupload;


    @PostMapping(value = "/overviewData") //new
    @ResponseBody
    public Msg overviewData(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        user =  userService.getUsers(user);
        JSONObject jsonObject = new JSONObject();
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        Imgreview imgreview = imgreviewService.selectByPrimaryKey(1);//查询非法个数
        Imgreview isImgreviewOK = imgreviewService.selectByusing(1);//查询有没有启动鉴别功能
        String ok = "false";
        jsonObject.put("myImgTotal", imgService.countimg(user.getId())); //我的图片数
        jsonObject.put("myAlbumTitle", albumService.selectAlbumCount(user.getId()));//我的画廊数量
        long memory = Long.valueOf(user.getMemory());//分配量
        Long usermemory = imgService.getusermemory(user.getId())==null?0L:imgService.getusermemory(user.getId());
        if(memory==0){
            jsonObject.put("myMemory","无容量");
        }else{
            Double aDouble = Double.valueOf(String.format("%.2f", (((double)usermemory/(double)memory)*100)));
            if(aDouble>=999){
                jsonObject.put("myMemory",999);
            }else{
                jsonObject.put("myMemory",aDouble);
            }
        }
        jsonObject.put("myMemorySum",SetFiles.readableFileSize(memory));
        if(user.getLevel()>1){
            ok = "true";
            //管理员
            jsonObject.put("imgTotal", imgService.counts(null) ); //站点图片数
            jsonObject.put("userTotal", userService.getUserTotal());
            jsonObject.put("ViolationImgTotal", imgreview.getCount()); //非法图片
            jsonObject.put("ViolationSwitch", isImgreviewOK==null?0:isImgreviewOK.getId()); //非法图片开关
            jsonObject.put("VisitorUpload", uploadConfig.getIsupdate());//是否禁用了游客上传
            jsonObject.put("VisitorMemory", SetFiles.readableFileSize(Long.valueOf(uploadConfig.getVisitormemory())));//访客共大小
            if(uploadConfig.getIsupdate()!=1){
                jsonObject.put("VisitorUpload", 0);//是否禁用了游客上传
                jsonObject.put("VisitorProportion",100.00);//游客用量%占比
                jsonObject.put("VisitorMemory", "禁用");//访客共大小
            }else{
                Long temp = imgService.getusermemory(0)==null?0:imgService.getusermemory(0);
                jsonObject.put("UsedMemory", (temp == null ? 0 : SetFiles.readableFileSize(temp)));//访客已用大小
                if(Long.valueOf(uploadConfig.getVisitormemory())==0){
                    jsonObject.put("VisitorProportion",100.00);//游客用量%占比
                }else if(Long.valueOf(uploadConfig.getVisitormemory())==-1){
                    jsonObject.put("VisitorProportion",0);//游客用量%占比
                    jsonObject.put("VisitorMemory", "无限");//访客共大小
                }else{
                    double sum = Double.valueOf(uploadConfig.getVisitormemory());
                    Double aDouble = Double.valueOf(String.format("%.2f", ((double) temp / sum) * 100));
                    if(aDouble>=999){
                        jsonObject.put("VisitorProportion",999);//游客用量%占比
                    }else{
                        jsonObject.put("VisitorProportion",aDouble);//游客用量%占比
                    }
                }
            }
        }
        jsonObject.put("ok", ok);
        msg.setData(jsonObject);
        return msg;
    }

    @PostMapping(value = "/SpaceExpansion")//new
    @ResponseBody
    public Msg SpaceExpansion(@RequestParam(value = "data", defaultValue = "") String data) {
        final Msg msg = new Msg();
        final JSONObject jsonObject = JSONObject.parseObject(data);
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        user =  userService.getUsers(user);
        if(user.getIsok()==0){
            msg.setCode("100403");
            msg.setInfo("你暂时无法使用此功能");
            return msg;
        }
        if(null==user){
            msg.setCode("100405");
            msg.setInfo("用户信息不存在");
            return msg;
        }else{
            long sizes = 0;
            Code code = codeService.selectCodekey(jsonObject.getString("code"));
            if(null==code){
                msg.setCode("100404");
                msg.setInfo("扩容码不存在,请重新填写");
                return msg;
            }
            Long userMemory = Long.valueOf(user.getMemory());
            sizes = Long.valueOf(code.getValue())+ userMemory;
            User newMemoryUser = new User();
            newMemoryUser.setMemory(Long.toString(sizes));
            newMemoryUser.setId(user.getId());
            userService.usersetmemory(newMemoryUser,jsonObject.getString("code"));
            msg.setInfo("你已成功扩容"+SetFiles.readableFileSize(sizes));
            return msg;
        }
    }

    @PostMapping("/getRecently")//new
    @ResponseBody
    public Msg getRecently(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        final JSONObject jsonObject = new JSONObject();
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            user =  userService.getUsers(user);
            if(user.getLevel()>1){
                jsonObject.put("RecentlyUser",imgService.RecentlyUser());
                jsonObject.put("RecentlyUploaded",imgService.RecentlyUploaded(user.getId()));
            }else{
                jsonObject.put("RecentlyUploaded",imgService.RecentlyUploaded(user.getId()));
            }
        }catch (Exception e){
            e.printStackTrace();
            msg.setInfo("系统内部错误");
            msg.setCode("500");
            return msg;
        }
        msg.setData(jsonObject);
        return msg;
    }

    @PostMapping("/getYyyy")//new
    @ResponseBody
    public Msg getYyyy(@RequestParam(value = "data", defaultValue = "") String data){
        final Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        User u = (User) subject.getPrincipal();
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("allYyyy",imgService.getyyyy(null));
        jsonObject.put("userYyyy",imgService.getyyyy(u.getId()));
        msg.setData(jsonObject);
        return msg;
    }

    @PostMapping("/getChart")//new
    @ResponseBody
    public Msg getChart(@RequestParam(value = "data", defaultValue = "") String data){
        Msg msg = new Msg();
        JSONObject jsonObject = JSONObject.parseObject(data);
        String yyyy = jsonObject.getString("yyyy");
        Integer type = jsonObject.getInteger("type");

        Subject subject = SecurityUtils.getSubject();
        User u = (User) subject.getPrincipal();
        List<Images> list =null;
        if(u.getLevel()>1){
            if(type==2){
                Images images = new Images();
                images.setYyyy(yyyy);
                list = imgService.countByM(images);
            }else{
                Images images = new Images();
                images.setYyyy(yyyy);
                images.setUserid(u.getId());
                list = imgService.countByM(images);
            }
        }else{
            Images images = new Images();
            images.setYyyy(yyyy);
            images.setUserid(u.getId());
            list = imgService.countByM(images);
        }
        JSONArray json = JSONArray.parseArray("[{\"id\":1,\"monthNum\":\"一月\",\"countNum\":0},{\"id\":2,\"monthNum\":\"二月\",\"countNum\":0},{\"id\":3,\"monthNum\":\"三月\",\"countNum\":0},{\"id\":4,\"monthNum\":\"四月\",\"countNum\":0},{\"id\":5,\"monthNum\":\"五月\",\"countNum\":0},{\"id\":6,\"monthNum\":\"六月\",\"countNum\":0},{\"id\":7,\"monthNum\":\"七月\",\"countNum\":0},{\"id\":8,\"monthNum\":\"八月\",\"countNum\":0},{\"id\":9,\"monthNum\":\"九月\",\"countNum\":0},{\"id\":10,\"monthNum\":\"十月\",\"countNum\":0},{\"id\":11,\"monthNum\":\"十一月\",\"countNum\":0},{\"id\":12,\"monthNum\":\"十二月\",\"countNum\":0}]");
        JSONArray jsonArray = new JSONArray();
        for (int j = 0; j < list.size(); j++) {
            for (int i = 0; i < json.size(); i++) {
                JSONObject jobj = json.getJSONObject(i);
                if(jobj.getInteger("id")==list.get(j).getMonthNum()){
                    jobj.put("monthNum",getChinaes(list.get(j).getMonthNum()));
                    jobj.put("countNum",list.get(j).getCountNum());
                }
            }
        }
        msg.setData(json);
        return msg;
    }

    @PostMapping("/getStorage")//new
    @ResponseBody
    public Msg getStorage() {
        Msg msg = new Msg();
        List<Keys> storage = keysService.getStorage();
        msg.setData(storage);
        return msg;
    }

    @PostMapping("/getStorageName")//new
    @ResponseBody
    public Msg getStorageName() {
        Msg msg = new Msg();
        List<Keys> storage = keysService.getStorageName();
        msg.setData(storage);
        return msg;
    }

    @PostMapping(value = "/selectPhoto")//new
    @ResponseBody
    public Msg selectPhoto(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        JSONObject jsonObj = JSONObject.parseObject(data);
        Integer pageNum = jsonObj.getInteger("pageNum");
        Integer pageSize = jsonObj.getInteger("pageSize");
        Integer source = jsonObj.getInteger("source");
        String starttime = jsonObj.getString("starttime");
        String stoptime = jsonObj.getString("stoptime");
        String selectUserType = jsonObj.getString("selectUserType");//me/all
        String username = jsonObj.getString("username");
        Integer selecttype = jsonObj.getInteger("selecttype");
        boolean violation = jsonObj.getBoolean("violation");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(starttime!=null){
            try {
                Date date1 = format.parse(starttime);
                Date date2 = format.parse(stoptime==null?format.format(new Date()):stoptime);
                int compareTo = date1.compareTo(date2);
                System.out.println(compareTo);
                if(compareTo==1){
                    msg.setCode("110500");
                    msg.setInfo("起始日期不能大于结束日期");
                    return msg;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                msg.setCode("110500");
                msg.setInfo("您输入的日期不正确");
                return msg;
            }
        }
        Images img = new Images();
        PageHelper.startPage(pageNum, pageSize);
        if(violation){
            img.setViolation("true");
        }
        img.setStarttime(starttime);
        img.setStoptime(stoptime);
        if(subject.hasRole("admin")){
            img.setSource(source);
            if(selectUserType.equals("me")){
                img.setUserid(user.getId());
                img.setUsername(null);
                img.setSelecttype(null);
            }else{
                img.setUsername(username);
                img.setSelecttype(selecttype);
            }
        }else{
            //普通用户
            img.setUserid(user.getId());
        }
        List<Images> images = imgService.selectimg(img);
        PageInfo<Images> rolePageInfo = new PageInfo<>(images);
        PageResultBean<Images> pageResultBean = new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
        msg.setData(pageResultBean);
        return msg;
    }


    @PostMapping(value = "/getUserInfo") //new
    @ResponseBody
    public Msg getUserInfo() {
        Msg msg = new Msg();
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            final User u = new User();
            u.setId(user.getId());
            User userInfo = userService.getUsers(u);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username",userInfo.getUsername());
            jsonObject.put("email",userInfo.getEmail());
            msg.setData(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("操作失败");
        }
        return msg;
    }

    @PostMapping("/setUserInfo") //new
    @ResponseBody
    public Msg setUserInfo(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            String username = jsonObject.getString("username");
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");
            Subject subject = SecurityUtils.getSubject();
            User u = (User) subject.getPrincipal();
            User user = new User();
            if(!SetText.checkEmail(email)){
                msg.setCode("110403");
                msg.setInfo("邮箱格式不正确");
                return msg;
            }
            String regex = "^\\w+$";
            if(username.length()>20 || !username.matches (regex)){
                msg.setCode("110403");
                msg.setInfo("用户名不得超过20位字符");
                return msg;
            }
            if(subject.hasRole("admin")){
                final User userOld = new User();
                userOld.setId(u.getId());
                User userInfo = userService.getUsers(userOld);
                if(!userInfo.getUsername().equals(username)){
                    Integer countusername = userService.countusername(username);
                    if(countusername == 1 || !SysName.CheckSysName(username)){
                        msg.setCode("110406");
                        msg.setInfo("此用户名已存在");
                        return msg;
                    }else {
                        user.setUsername(username);
                    }
                }
                if(!userInfo.getEmail().equals(email)){
                    Integer countmail = userService.countmail(email);
                    if(countmail == 1){
                        msg.setCode("110407");
                        msg.setInfo("此邮箱已被注册");
                        return msg;
                    }else{
                        user.setEmail(email);
                    }
                }
                user.setPassword(Base64Encryption.encryptBASE64(password.getBytes()));
                user.setUid(u.getUid());
            }else{
                user.setPassword(Base64Encryption.encryptBASE64(password.getBytes()));
                user.setUid(u.getUid());
            }
            userService.change(user);
            msg.setInfo("信息修改成功，请重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("服务执行异常，请稍后再试");
        }
        return msg;
    }

    @PostMapping("/deleImages") //new
    @ResponseBody
    public Msg deleImages(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        JSONArray images = jsonObj.getJSONArray("images");
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();

        if(null == user){
            msg.setCode("500");
            msg.setInfo("当前用户信息不存在");
            return msg;
        }
        if(images.size()==0){
            msg.setCode("404");
            msg.setInfo("为获取到图像信息");
            return msg;
        }
        for (int i = 0; i < images.size(); i++) {
            Integer imgid = images.getInteger(i);
            Images image = imgService.selectByPrimaryKey(imgid);
            Integer keyid = image.getSource();
            String imgname = image.getImgname();
            Keys key = keysService.selectKeys(keyid);

            if(!subject.hasRole("admin")){
                if(image.getUserid()!=user.getId()){
                    break;
                }
            }
            boolean isDele = false;
            try{
                if (key.getStorageType() == 1) {
                    isDele = nosImageupload.delNOS(key.getId(), imgname);
                } else if (key.getStorageType() == 2) {
                    isDele = ossImageupload.delOSS(key.getId(), imgname);
                } else if (key.getStorageType() == 3) {
                    isDele = ussImageupload.delUSS(key.getId(), imgname);
                } else if (key.getStorageType() == 4) {
                    isDele = kodoImageupload.delKODO(key.getId(), imgname);
                } else if (key.getStorageType() == 5) {
                    isDele = LocUpdateImg.deleteLOCImg(imgname);
                }else if (key.getStorageType() == 6) {
                    isDele = cosImageupload.delCOS(key.getId(), imgname);
                }else if (key.getStorageType() == 7) {
                    isDele = ftpImageupload.delFTP(key.getId(), imgname);
                }else if (key.getStorageType() == 8) {
                    isDele = uFileImageupload.delUFile(key.getId(), imgname);
                }else {
                    System.err.println("未获取到对象存储参数，删除失败");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            if(isDele){
                try {
                    imgTempService.delImgAndExp(image.getImguid());
                    imgService.deleimg(imgid);
                    imgAndAlbumService.deleteImgAndAlbum(imgname);
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.setInfo("图片记录删除失败，请重试");
                    msg.setCode("500");
                    return msg;
                }
                msg.setInfo("删除成功");
            }else{
                imgTempService.delImgAndExp(image.getImguid());
                imgService.deleimg(imgid);
                imgAndAlbumService.deleteImgAndAlbum(imgname);
                msg.setInfo("图片记录已删除，但是图片源删除失败");
            }
        }
        return msg;
    }

    //工具函数
    private static String getChinaes(int v){
        String ch = "";
        switch(v){
            case 1 :
                ch = "一月";
                break; //可选
            case 2 :
                ch = "二月";
                break; //可选
            case 3 :
                ch = "三月";
                break; //可选
            case 4 :
                ch = "四月";
                break; //可选
            case 5 :
                ch = "五月";
                break; //可选
            case 6 :
                ch = "六月";
                break; //可选
            case 7 :
                ch = "七月";
                break; //可选
            case 8 :
                ch = "八月";
                break; //可选
            case 9 :
                ch = "九月";
                break; //可选
            case 10 :
                ch = "十月";
                break; //可选
            case 11 :
                ch = "十一月";
                break; //可选
            case 12 :
                ch = "十二月";
                break; //可选
            default : ch = "";//可选
                //语句
        }

        return ch;

    }

}
