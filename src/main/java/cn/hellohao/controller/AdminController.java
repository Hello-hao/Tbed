package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.pojo.vo.PageResultBean;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.ImgServiceImpl;
import cn.hellohao.service.impl.UserServiceImpl;
import cn.hellohao.utils.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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


    @RequestMapping(value = "/goadmin")
    public String goadmin1(HttpSession session, Model model, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
        Integer usermemory = imgService.getusermemory(user.getId());
        if(usermemory==null){usermemory=0;}
        User u = userService.getUsers(user.getEmail());
        if(user!=null){
            if (user.getLevel() == 1) {
                model.addAttribute("level", "普通用户");
            } else if (user.getLevel() == 2) {
                model.addAttribute("level", "管理员");
            } else {
                model.addAttribute("level", "未 知");
            }
            model.addAttribute("levels", user.getLevel());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("api", uploadConfig.getApi());

            model.addAttribute("memory",u.getMemory());//单位M
            if(usermemory==null){
                model.addAttribute("usermemory", 0);//单位M
            }else{
                float d = (float) (Math.round((usermemory/1024.0F) * 100.0) / 100.0);
                model.addAttribute("usermemory", d);//单位M
            }
            return "admin/index";
        }else{
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/admin")
    public String goadmin(HttpSession session, Model model) {
        Config config = configService.getSourceype();
        User u = (User) session.getAttribute("user");
        model.addAttribute("username", u.getUsername());
        model.addAttribute("level", u.getLevel());
        model.addAttribute("email", u.getEmail());
        model.addAttribute("loginid", 100);

        return "admin/table";
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
        Integer usermemory = imgService.getusermemory(u.getId());
        if(usermemory==null){usermemory=0;}
        User user = userService.getUsers(u.getEmail());
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
                //Print.Normal();
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
            jsonObject.put("VisitorUpload", uploadConfig.getIsupdate());//是否禁用了游客上传
            jsonObject.put("UsedSize", (imgService.getusermemory(0)/1024));//访客已用大小
            jsonObject.put("VisitorMemory", uploadConfig.getVisitormemory());//访客共大小
        }
        return jsonObject.toString();
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
            } else {
                img.setUserid(u.getId());
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
    public String deleimg(HttpSession session, Integer id, Integer sourcekey) {
        JSONObject jsonObject = new JSONObject();
        User u = (User) session.getAttribute("user");
        Images images = imgService.selectByPrimaryKey(id);
        Keys key = keysService.selectKeys(sourcekey);
        Integer Sourcekey = GetCurrentSource.GetSource(u.getId());
        Boolean b =false;
        if(Sourcekey==5){
            b =true;
        }else{
            b = StringUtils.doNull(Sourcekey,key);//判断对象是否有空值
        }
        if(b){
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
            }else {
                System.err.println("未获取到对象存储参数，删除失败。");
            }
            Integer ret = imgService.deleimg(id);
            Integer count = 0;
            if (ret > 0) {
                jsonObject.put("usercount", imgService.countimg(u.getId()));
                jsonObject.put("count", imgService.counts(null));
                count = 1;
            } else {
                count = 0;
            }
            jsonObject.put("val", count);
        }else{
            jsonObject.put("val", 0);
        }
        return jsonObject.toString();
    }

    @PostMapping("/deleallimg")
    @ResponseBody
    public String deleallimg(HttpSession session, @RequestParam("ids[]") Integer[] ids) {
        JSONObject jsonObject = new JSONObject();
        Integer v = 0;
        ImgServiceImpl de = new ImgServiceImpl();
        User u = (User) session.getAttribute("user");
        Integer Sourcekey = GetCurrentSource.GetSource(u.getId());
        for (int i = 0; i < ids.length; i++) {
            Images images = imgService.selectByPrimaryKey(ids[i]);
            Keys key = keysService.selectKeys(images.getSource());
            Boolean b =false;
            if(Sourcekey==5){
                b =true;
            }else{
                b = StringUtils.doNull(Sourcekey,key);//判断对象是否有空值
            }
            if(b){
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
                }else {
                    System.err.println("未获取到对象存储参数，删除失败。");
                }
                Integer ret = imgService.deleimg(ids[i]);
                if (ret < 1) {
                    v = 0;
                } else {
                    v = 1;
                }
            }else {
                v = 0;
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
        // -1 用户名重复
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
        User u1 = userService.getUsers(u.getEmail());
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


}
