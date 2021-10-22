package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.pojo.vo.PageResultBean;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.AlbumServiceI;
import cn.hellohao.service.impl.ImgServiceImpl;
import cn.hellohao.service.impl.UserServiceImpl;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private UserService userService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private ImgreviewService imgreviewService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private ImgAndAlbumService imgAndAlbumService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    AlbumServiceI albumServiceI;

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
        //普通用户
        String ok = "false";
        jsonObject.put("myToken","这个去掉");
        jsonObject.put("myImgTotal", imgService.countimg(user.getId())); //我的图片数
        jsonObject.put("myAlbumTitle", albumService.selectAlbumCount(user.getId()));//我的画廊数量
        jsonObject.put("myView360Title", "0");//我的全景视图
        //计算自己的百分比 已用量/分配量
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
            jsonObject.put("imgTotal", imgService.counts(null) ); //admin  站点图片数
            jsonObject.put("userTotal", userService.getUserTotal()); //admin  用户个数
            jsonObject.put("ViolationImgTotal", imgreview.getCount()); //admin 非法图片
            jsonObject.put("ViolationSwitch", isImgreviewOK==null?0:isImgreviewOK.getId()); //admin 非法图片开关
            jsonObject.put("VisitorUpload", uploadConfig.getIsupdate());//是否禁用了游客上传
            jsonObject.put("VisitorMemory", SetFiles.readableFileSize(Long.valueOf(uploadConfig.getVisitormemory())));//访客共大小
            if(uploadConfig.getIsupdate()!=1){
                jsonObject.put("VisitorUpload", 0);//是否禁用了游客上传
                jsonObject.put("VisitorProportion",100.00);//游客用量%占比
                jsonObject.put("VisitorMemory", "禁用");//访客共大小
            }else{
                Long temp = imgService.getusermemory(0)==null?0:imgService.getusermemory(0);
                jsonObject.put("UsedMemory", (temp == null ? 0 : SetFiles.readableFileSize(temp)));//访客已用大小
                if(Integer.valueOf(uploadConfig.getVisitormemory())==0){
                    jsonObject.put("VisitorProportion",100.00);//游客用量%占比
                }else if(Integer.valueOf(uploadConfig.getVisitormemory())==-1){
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
        //Config config = configService.getSourceype();
        msg.setData(jsonObject);
        return msg;
    }

    @RequestMapping(value = "/tosurvey")
    public String admin2(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        String sysetmname = System.getProperty("os.name");
        String isarch = System.getProperty("os.arch");
        String jdk = System.getProperty("java.version");
        model.addAttribute("username", u.getUsername());
        model.addAttribute("levels", u.getLevel());
        model.addAttribute("sysetmname",sysetmname);
        model.addAttribute("isarch", isarch);
        model.addAttribute("jdk", jdk);
        //空间大小
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        Long usermemory = imgService.getusermemory(u.getId());
        if(usermemory==null){usermemory=0L;}
        User user = userService.getUsers(u);
        if(u!=null){
            if (u.getLevel() == 1) {
                model.addAttribute("level", "普通用户");
            } else if (u.getLevel() == 2) {
                model.addAttribute("level", "管理员");
            } else {
                model.addAttribute("level", "未 知");
            }
            model.addAttribute("levels", u.getLevel());
            model.addAttribute("username", u.getUsername());
            model.addAttribute("api", uploadConfig.getApi());

            model.addAttribute("memory",user.getMemory());//单位M
            if(usermemory==null){
                model.addAttribute("usermemory", 0);//单位M
            }else{
                float d = (float) (Math.round((usermemory/1024.0F) * 100.0) / 100.0);
                model.addAttribute("usermemory", d);//单位M
            }
        }
        return "admin/survey";
    }

    @RequestMapping(value = "/getwebconfig")
    @ResponseBody
    public String getwebconfig(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        //Config config = configService.getSourceype();
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        User u = (User) session.getAttribute("user");
        Imgreview imgreview = imgreviewService.selectByPrimaryKey(1);
        jsonObject.put("usercount", imgService.countimg(u.getId()));
        jsonObject.put("counts", imgService.counts(null) );
        jsonObject.put("getusertotal", userService.getUserTotal());
        jsonObject.put("imgreviewcount", imgreview.getCount());
        if(uploadConfig.getIsupdate()!=1){
            jsonObject.put("VisitorUpload", 0);//是否禁用了游客上传
        }else{
            Long temp = imgService.getusermemory(0);
            jsonObject.put("VisitorUpload", uploadConfig.getIsupdate());//是否禁用了游客上传
            jsonObject.put("UsedSize", (temp == null ? 0 : temp/1024));//访客已用大小
            jsonObject.put("VisitorMemory", uploadConfig.getVisitormemory());//访客共大小
        }
        return jsonObject.toString();
    }

    @PostMapping("/getRecently")//new 获取榜单和最近上传
    @ResponseBody
    public Msg getRecently(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        final JSONObject jsonObject = new JSONObject();
        try {
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            user =  userService.getUsers(user);
            if(user.getLevel()>1){
                //管理员 可以看榜单数据 和最近上传
                jsonObject.put("RecentlyUser",imgService.RecentlyUser());
                jsonObject.put("RecentlyUploaded",imgService.RecentlyUploaded(user.getId()));
            }else{
                //普通用户只能看最近上传
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




    @RequestMapping(value = "/selecttable")
    @ResponseBody
    public PageResultBean<Images> selectByFy(HttpSession session, Integer pageNum, Integer pageSize, Integer selecttype,
                                             Integer storageType,String starttime,String stoptime) {
        User u = (User) session.getAttribute("user");
        Images img = new Images();
        if(storageType!=null){
            if(storageType!=0){
                img.setSource(storageType);
            }
        }
        if(starttime!=null && stoptime!=null){
            if(!starttime.equals("") && !stoptime.equals("")){
                img.setStarttime(starttime);
                img.setStoptime(stoptime);
            }
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Images> images = null;
        if (u.getLevel() > 1) {
            if (selecttype == 1) {
                images = imgService.selectimg(img);
            } else if(selecttype == 2) {
                img.setUserid(u.getId());
                images = imgService.selectimg(img);
            }else{
                img.setUserid(u.getId());
                img.setSelecttype(3);
                images = imgService.selectimg(img);
            }
        } else {
            img.setUserid(u.getId());
            images = imgService.selectimg(img);
        }
        PageInfo<Images> rolePageInfo = new PageInfo<>(images);
        return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
    }

    @RequestMapping(value = "/selectusertable")
    @ResponseBody
    public Map<String, Object> selectByFy12(HttpSession session, @RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false) int limit,String username) {
        User u = (User) session.getAttribute("user");
        PageHelper.startPage(page, limit);
        List<User> users = null;
        if (u.getLevel() > 1) {
            User user = new User();
            user.setUsername(username);
            users = userService.getuserlist(user);
            PageInfo<User> rolePageInfo = new PageInfo<>(users);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 0);
            map.put("msg", "");
            map.put("count", rolePageInfo.getTotal());
            map.put("data", rolePageInfo.getList());
            return map;
        } else {
            return null;
        }
    }

    @PostMapping("/deleimg")
    @ResponseBody
    public String deleimg(HttpSession session, Integer id, Integer sourcekey,String imgname) {
        JSONObject jsonObject = new JSONObject();
        User u = (User) session.getAttribute("user");
        Images images = imgService.selectByPrimaryKey(id);
        Keys key = keysService.selectKeys(sourcekey);
            ImgServiceImpl de = new ImgServiceImpl();
            if (key.getStorageType() == 1) {
                de.delect(key, images.getImgname());
            } else if (key.getStorageType() == 2) {
                de.delectOSS(key, images.getImgname());
            } else if (key.getStorageType() == 3) {
                de.delectUSS(key, images.getImgname());
            } else if (key.getStorageType() == 4) {
                de.delectKODO(key, images.getImgname());
            } else if (key.getStorageType() == 5) {
                LocUpdateImg.deleteLOCImg(images.getImgname());
            }else if (key.getStorageType() == 6) {
                de.delectCOS(key, images.getImgname());
            }else if (key.getStorageType() == 7) {
                de.delectFTP(key, images.getImgname());
            }else if (key.getStorageType() == 8) {
                de.delectUFile(key, images.getImgname());
            }else {
                System.err.println("未获取到对象存储参数，删除失败。");
            }
            Integer ret = imgService.deleimg(id);
            if(ret>0){ imgAndAlbumService.deleteImgAndAlbum(imgname);}
            Integer count = 0;
            if (ret > 0) {
                jsonObject.put("usercount", imgService.countimg(u.getId()));
                jsonObject.put("count", imgService.counts(null));
                count = 1;
                imgAndAlbumService.deleteImgAndAlbum(imgname);//关联表删除关于这张照片的所有记录
            } else {
                count = 0;
            }
            jsonObject.put("val", count);
        return jsonObject.toString();
    }

    @PostMapping("/deleallimg")
    @ResponseBody
    public String deleallimg(HttpSession session, @RequestParam("ids[]") Integer[] ids,@RequestParam("sources[]") Integer[] sources,
                             @RequestParam("imgnames[]") String[] imgnames) {
        JSONObject jsonObject = new JSONObject();
        Integer v = 0;
        ImgServiceImpl de = new ImgServiceImpl();
        User u = (User) session.getAttribute("user");
        for (int i = 0; i < ids.length; i++) {
            Keys key = keysService.selectKeys(sources[i]);
                if (key.getStorageType() == 1) {
                    de.delect(key, imgnames[i]);
                } else if (key.getStorageType() == 2) {
                    de.delectOSS(key, imgnames[i]);
                } else if (key.getStorageType() == 3) {
                    de.delectUSS(key, imgnames[i]);
                } else if (key.getStorageType() == 4) {
                    de.delectKODO(key, imgnames[i]);
                } else if (key.getStorageType() == 5) {
                    LocUpdateImg.deleteLOCImg(imgnames[i]);
                }else if (key.getStorageType() == 6) {
                    de.delectCOS(key, imgnames[i]);
                }else if (key.getStorageType() == 7) {
                    de.delectFTP(key, imgnames[i]);
                }else if (key.getStorageType() == 8) {
                    de.delectUFile(key, imgnames[i]);
                }else {
                    System.err.println("未获取到对象存储参数，删除失败。");
                }
                Integer ret = imgService.deleimg(ids[i]);
                if (ret < 1) {
                    v = 0;
                } else {
                    v = 1;
                    imgAndAlbumService.deleteImgAndAlbum(imgnames[i]);//关联表删除关于这张照片的所有记录
                }
        }
        jsonObject.put("val", v);
        jsonObject.put("usercount", imgService.countimg(u.getId()));
        jsonObject.put("count", imgService.counts(null));
        return jsonObject.toString();
    }

    @RequestMapping(value = "/tosetuser")
    public String tosetuser(HttpSession session, Model model, HttpServletRequest request) {
        User u = (User) session.getAttribute("user");
        model.addAttribute("user", u);
        return "admin/setuser";
    }

    @PostMapping("/change")
    @ResponseBody
    public String change(HttpSession session, User user) {
        User u = (User) session.getAttribute("user");
        User us = new User();
        us.setEmail(user.getEmail());
        us.setUsername(user.getUsername());
        us.setPassword(Base64Encryption.encryptBASE64(user.getPassword().getBytes()));
        us.setUid(u.getUid());
        JSONArray jsonArray = new JSONArray();
        Integer ret =0;
        if(u.getLevel()==1){
            us.setUsername(null);
            us.setEmail(null);
            ret = userService.change(us);
        }else{
            ret = userService.change(us);
        }
        jsonArray.add(ret);
        if (u.getEmail() != null && u.getPassword() != null) {
            session.removeAttribute("user");
            session.invalidate();
        }
        return jsonArray.toString();
    }
    //进入api
    @RequestMapping(value = "/toapi")
    public String toapi(HttpSession session, Model model, HttpServletRequest request) {
        User u = (User) session.getAttribute("user");
        Config config = configService.getSourceype();
        //key信息
        model.addAttribute("username", u.getUsername());
        model.addAttribute("level", u.getLevel());
        model.addAttribute("domain", config.getDomain());
        return "admin/api";
    }

    @PostMapping(value = "/kuorong")
    @ResponseBody
    public String kuorong(HttpSession session, String codestring) {
        User u = (User) session.getAttribute("user");
        JSONObject jsonObject = new JSONObject();
        User u1 = userService.getUsers(u);
        Integer ret =0;
        Integer sizes = 0;
        if(u!=null){
            Code c = codeService.selectCodekey(codestring);
            Print.warning(c);
            if(c!=null) {
                User user = new User();
                sizes = c.getValue()+u1.getMemory();
                user.setMemory(c.getValue()+u1.getMemory());
                user.setId(u.getId());
                ret = userServiceImpl.usersetmemory(user,codestring);
                if(ret>0){ ret = 1; }
            }else{
                ret=-1;
            }
        }else{
            //登录过期
            session.invalidate();
        }
        jsonObject.put("sizes",sizes);
        jsonObject.put("ret",ret);
        return jsonObject.toString();
    }

    @GetMapping(value = "/images/{id}")
    @ResponseBody
    public Images selectByFy(@PathVariable("id") Integer id) {
        return imgService.selectByPrimaryKey(id);
    }


    @RequestMapping(value = "/albumlist")
    public String albumlist(HttpSession session, Model model) {
        Config config = configService.getSourceype();
        User u = (User) session.getAttribute("user");
        model.addAttribute("config", config);
        model.addAttribute("level", u.getLevel());
        model.addAttribute("email", u.getEmail());
        model.addAttribute("loginid", 100);

        return "admin/albumlist";
    }

    @PostMapping("/getAlbumURLList")
    @ResponseBody
    public Map<String, Object> getAlbumURLList (HttpSession session,@RequestParam(required = false, defaultValue = "1") int page,
    @RequestParam(required = false) int limit,Album album){
        User u = (User) session.getAttribute("user");
        PageHelper.startPage(page, limit);
        List<Album>  list = null;
        if (u.getLevel() == 2) {
            album.setUserid(null);
            list = albumServiceI.selectAlbumURLList(album);
            PageInfo<Album> rolePageInfo = new PageInfo<>(list);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 0);
            map.put("msg", "");
            map.put("count", rolePageInfo.getTotal());
            map.put("data", rolePageInfo.getList());
            return map;
        } else {
            album.setUserid(u.getId());
            list = albumServiceI.selectAlbumURLList(album);
            PageInfo<Album> rolePageInfo = new PageInfo<>(list);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 0);
            map.put("msg", "");
            map.put("count", rolePageInfo.getTotal());
            map.put("data", rolePageInfo.getList());
            return map;
        }
    }

    @RequestMapping("/delAlbum")
    @ResponseBody
    public Integer delAlbum(String albumkey){
        Integer ret = albumServiceI.delete(albumkey);
        return ret;
    }

    @RequestMapping("/delAlbumAll")
    @ResponseBody
    public Integer delAlbumAll( @RequestParam("albumkeyArr[]") String[] albumkeyArr ){
        Integer ret = albumServiceI.deleteAll(albumkeyArr);
        return ret;
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
