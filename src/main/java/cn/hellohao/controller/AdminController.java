package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.pojo.vo.PageResultBean;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.ImgServiceImpl;

import cn.hellohao.utils.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ImgreviewService imgreviewService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private EmailConfigService emailConfigService;


    @RequestMapping(value = "/goadmin")
    public String goadmin1(HttpSession session, Model model, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        if (user.getLevel() == 1) {
            model.addAttribute("level", "普通用户");
        } else if (user.getLevel() == 2) {
            model.addAttribute("level", "管理员");
        } else {
            model.addAttribute("level", "未 知");
        }
        model.addAttribute("levels", user.getLevel());
        model.addAttribute("username", user.getUsername());
        return "admin/index";
    }


    // 进入后台页面
    @RequestMapping(value = "/admin")
    public String goadmin(HttpSession session, Model model, HttpServletRequest request) {
        Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
        Keys key = keysService.selectKeys(config.getSourcekey());
        User u = (User) session.getAttribute("user");
        Imgreview imgreview = imgreviewService.selectByPrimaryKey(1);
//        if (u.getLevel() > 1) {
//            //管理员
//            model.addAttribute("counts", imgService.counts(null));
//        } else {
//            //个人用户
//            //model.addAttribute("counts", imgService.countimg(u.getId()));//这个是根据用户id查询他的图片数
//
//        }
        model.addAttribute("usercount", imgService.countimg(u.getId()));//这个是根据用户id查询他的图片数
        model.addAttribute("counts", imgService.counts(null) );//总数
        model.addAttribute("getUserTotal", userService.getUserTotal() );
        model.addAttribute("imgreviewcount", imgreview.getCount());
        model.addAttribute("username", u.getUsername());
        model.addAttribute("level", u.getLevel());
        model.addAttribute("email", u.getEmail());
        model.addAttribute("loginid", 100);
        Boolean b = StringUtils.doNull(key);//判断对象是否有空值
        if(b){
            model.addAttribute("source", key.getStorageType());
        }else{
            model.addAttribute("source", 456);
        }
        return "admin/table";
    }

    @RequestMapping(value = "/selecttable")
    @ResponseBody
    public PageResultBean<Images> selectByFy(HttpSession session, Integer pageNum, Integer pageSize, Integer selecttype) {
        User u = (User) session.getAttribute("user");
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(pageNum, pageSize);
        List<Images> images = null;
        if (u.getLevel() > 1) { //根据用户等级查询管理员查询所有的信息
            if (selecttype == 1) {
                images = imgService.selectimg(null);// 这是我们的sql
            } else {
                images = imgService.selectimg(u.getId());// 这是我们的sql
            }
        } else {
            images = imgService.selectimg(u.getId());// 这是我们的sql
        }
        // 使用pageInfo包装查询
        PageInfo<Images> rolePageInfo = new PageInfo<>(images);//
        return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
    }

    //获取用户信息列表
    @RequestMapping(value = "/selectusertable")
    @ResponseBody
    public PageResultBean<User> selectByFy1(HttpSession session, Integer pageNum, Integer pageSize) {
        User u = (User) session.getAttribute("user");
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = null;
        if (u.getLevel() > 1) { //根据用户等级查询管理员查询所有的信息
            users = userService.getuserlist();// 这是我们的sql
            // 使用pageInfo包装查询
            PageInfo<User> rolePageInfo = new PageInfo<>(users);//
            return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
        } else {
            return null;
        }
    }

    //返回对象存储界面
    @RequestMapping(value = "/root/touser")
    public String touser() {
        return "admin/user";
    }

    //获取用户信息列表
    @RequestMapping(value = "/selectusertable2")
    @ResponseBody
    public Map<String, Object> selectByFy12(HttpSession session, @RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false) int limit) {
        User u = (User) session.getAttribute("user");
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(page, limit);
        List<User> users = null;
        if (u.getLevel() > 1) { //根据用户等级查询管理员查询所有的信息
            users = userService.getuserlist();// 这是我们的sql
            // 使用pageInfo包装查询
            PageInfo<User> rolePageInfo = new PageInfo<>(users);//
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

    //返回对象存储界面
    @RequestMapping(value = "/root/tostorage")
    public String tostorage(HttpSession session, Model model, HttpServletRequest request) {
        Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
        Keys key = keysService.selectKeys(config.getSourcekey());
        Boolean b = StringUtils.doNull(key);//判断对象是否有空值
        if(b){
            //key信息
            model.addAttribute("AccessKey", key.getAccessKey());
            model.addAttribute("AccessSecret", key.getAccessSecret());
            model.addAttribute("Endpoint", key.getEndpoint());
            model.addAttribute("Bucketname", key.getBucketname());
            model.addAttribute("RequestAddress", key.getRequestAddress());
            model.addAttribute("StorageType", config.getSourcekey());
        }
        return "admin/storageconfig";
    }

    //根据下拉框选的存储源查询对应的key
    @PostMapping("/root/getkey")
    @ResponseBody
    public String getkey(Integer storageType) {
        JSONArray jsonArray = new JSONArray();
        Keys key = keysService.selectKeys(storageType);
        jsonArray.add(key);
        return jsonArray.toString();
    }

    @PostMapping("/root/updatekey")
    @ResponseBody
    public String updatekey(Keys key) {
        JSONArray jsonArray = new JSONArray();
        Config config = new Config();
        config.setSourcekey(key.getStorageType());
        Integer val = configService.setSourceype(config);
        if (val > 0) {
            Integer ret = keysService.updateKey(key);
            jsonArray.add(ret);
        } else {
            jsonArray.add(0);
        }
        return jsonArray.toString();
    }

    @PostMapping("/deleimg")
    @ResponseBody
    public String deleimg(HttpSession session, Integer id, Integer sourcekey) {
        JSONObject jsonObject = new JSONObject();
        User u = (User) session.getAttribute("user");
        Images images = imgService.selectByPrimaryKey(id);
        Keys key = keysService.selectKeys(sourcekey);
        Boolean b = StringUtils.doNull(key);//判断对象是否有空值
        if(b){
            ImgServiceImpl de = new ImgServiceImpl();

            if (key.getStorageType() == 1) {
                de.delect(key, images.getImgname());
            } else if (key.getStorageType() == 2) {
                de.delectOSS(key, images.getImgname());
            } else if (key.getStorageType() == 3) {
                //初始化腾讯云
            } else if (key.getStorageType() == 4) {
                //初始化七牛云
            } else {
                System.err.println("未获取到对象存储参数，删除失败。");
            }
            Integer ret = imgService.deleimg(id);
            Integer count = 0;
            if (ret > 0) {
                jsonObject.put("usercount", imgService.countimg(u.getId()));
                jsonObject.put("count", imgService.counts(null) + 1000);
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

    //批量删除图片
    @PostMapping("/deleallimg")
    @ResponseBody
    public String deleallimg(HttpSession session, @RequestParam("ids[]") Integer[] ids) {
        JSONObject jsonObject = new JSONObject();
        Integer v = 0;
        ImgServiceImpl de = new ImgServiceImpl();
        User u = (User) session.getAttribute("user");

        for (int i = 0; i < ids.length; i++) {
            Images images = imgService.selectByPrimaryKey(ids[i]);
            Keys key = keysService.selectKeys(images.getSource());
            Boolean b = StringUtils.doNull(key);//判断对象是否有空值
            if(b){
                if (key.getStorageType() == 1) {
                    de.delect(key, images.getImgname());
                } else if (key.getStorageType() == 2) {
                    de.delectOSS(key, images.getImgname());
                } else if (key.getStorageType() == 3) {
                    //初始化腾讯云
                } else if (key.getStorageType() == 4) {
                    //初始化七牛云
                } else {
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
        jsonObject.put("count", imgService.counts(null) + 1000);

        return jsonObject.toString();
    }

    //刪除用戶
    @PostMapping("/root/deleuser")
    @ResponseBody
    public String deleuser(HttpSession session, Integer id) {
        JSONArray jsonArray = new JSONArray();
        Integer ret = userService.deleuser(id);
        jsonArray.add(ret);
        return jsonArray.toString();
    }

    //进入修改密码页面
    @RequestMapping(value = "/tosetuser")
    public String tosetuser(HttpSession session, Model model, HttpServletRequest request) {
        User u = (User) session.getAttribute("user");
        //key信息
        model.addAttribute("username", u.getUsername());
//
        return "admin/setuser";
    }

    //修改资料
    @PostMapping("/change")
    @ResponseBody
    public String change(HttpSession session, User user) {
        //Integer count = userService.checkUsername(user.getUsername());//查询用户名是否重复
        User u = (User) session.getAttribute("user");
        user.setEmail(u.getEmail());
        JSONArray jsonArray = new JSONArray();
        Integer ret = userService.change(user);
        jsonArray.add(ret);
        if (u.getEmail() != null && u.getPassword() != null) {
            session.removeAttribute("user");
            //刷新view
            session.invalidate();
        }
        // -1 用户名重复
        return jsonArray.toString();
    }

    //跳转邮箱配置页面
    @RequestMapping(value = "/root/emailconfig")
    public String emailconfig(Model model) {
        EmailConfig emailConfig = emailConfigService.getemail();
        model.addAttribute("emailConfig",emailConfig);
        return "admin/emailconfig";
    }
    //修改邮箱验证
    @PostMapping("/root/updateemail")
    @ResponseBody
    public Integer updateemail(HttpSession session,String emails, String emailkey, String emailurl, String port, String emailname, Integer using ) {
        EmailConfig emailConfig = new EmailConfig();
        emailConfig.setEmailname(emailname);
        emailConfig.setEmails(emails);
        emailConfig.setEmailkey(emailkey);
        emailConfig.setEmailurl(emailurl);
        emailConfig.setPort(port);
        emailConfig.setUsing(using);
        Integer ret = emailConfigService.updateemail(emailConfig);
        return ret;
    }
    //跳转系统配置页面
    @RequestMapping(value = "/root/towebconfig")
    public String towebconfig(Model model) {
        Config config = configService.getSourceype();
        model.addAttribute("config",config);
        return "admin/webconfig";
    }
    //修改站点配置
    @PostMapping("/root/updateconfig")
    @ResponseBody
    public Integer updateconfig(HttpSession session, String webname,String explain, String logos,
                                String footed, String links, String notice,String baidu,String domain ) {
        System.err.println("域名=="+domain);
        Config config = new Config();
        config.setWebname(webname);
        config.setExplain(explain);
        config.setLogos(logos);
        config.setFooted(footed);
        config.setLinks(links);
        config.setNotice(notice);
        config.setBaidu(baidu);
        config.setDomain(domain);
        Integer ret = configService.setSourceype(config);
        return ret;
    }

    @GetMapping(value = "/images/{id}")
    @ResponseBody
    public Images selectByFy(@PathVariable("id") Integer id) {
        return imgService.selectByPrimaryKey(id);
    }


}
