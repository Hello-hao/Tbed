package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.pojo.vo.PageResultBean;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.ImgServiceImpl;
import cn.hellohao.utils.LocUpdateImg;
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


    @RequestMapping(value = "/goadmin")
    public String goadmin1(HttpSession session, Model model, HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
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
            return "admin/index";
        }else{
            return "redirect:/";
        }
    }

    // 进入后台页面
    @RequestMapping(value = "/admin")
    public String goadmin(HttpSession session, Model model) {
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
//        }

        //model.addAttribute("usercount", imgService.countimg(u.getId()));//这个是根据用户id查询他的图片数
        //model.addAttribute("counts", imgService.counts(null) );//总数
        //model.addAttribute("getusertotal", userService.getUserTotal() );
        //model.addAttribute("imgreviewcount", imgreview.getCount());
        model.addAttribute("username", u.getUsername());
        model.addAttribute("level", u.getLevel());
        model.addAttribute("email", u.getEmail());
        model.addAttribute("loginid", 100);
        Boolean b = StringUtils.doNull(key);//判断对象是否有空值
//        if(b){
//            model.addAttribute("source", key.getStorageType());
//        }else{
//            model.addAttribute("source", 456);
//        }
        return "admin/table";
    }
    // 进入后台页面
    @RequestMapping(value = "/tosurvey")
    public String admin2(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");
        String sysetmname = System.getProperty("os.name");
        String isarch = System.getProperty("os.arch");
        String jdk = System.getProperty("java.version");
        model.addAttribute("username", u.getUsername());
        model.addAttribute("sysetmname",sysetmname);
        model.addAttribute("isarch", isarch);
        model.addAttribute("jdk", jdk);
        return "admin/survey";
    }
    //获取本站概况
    @RequestMapping(value = "/getwebconfig")
    @ResponseBody
    public String getwebconfig(HttpSession session) {
        JSONObject jsonObject = new JSONObject();
        Config config = configService.getSourceype();//查询当前系统使用的存储源类型。
        User u = (User) session.getAttribute("user");
        Imgreview imgreview = imgreviewService.selectByPrimaryKey(1);
        jsonObject.put("usercount", imgService.countimg(u.getId()));//这个是根据用户id查询他的图片数
        jsonObject.put("counts", imgService.counts(null) );//总数
        jsonObject.put("getusertotal", userService.getUserTotal() );
        jsonObject.put("imgreviewcount", imgreview.getCount());
        Keys key= keysService.selectKeys(config.getSourcekey());//然后根据类型再查询key
        Boolean b = StringUtils.doNull(key);//判断对象是否有空值
        if(b){
            jsonObject.put("source", key.getStorageType());
        }else{
            jsonObject.put("source", 456);
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
        // 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
        // 从他的下一条sql开始分页
        PageHelper.startPage(pageNum, pageSize);
        List<Images> images = null;
        if (u.getLevel() > 1) { //根据用户等级查询管理员查询所有的信息
            if (selecttype == 1) {
                images = imgService.selectimg(img);// 这是我们的sql
            } else {
                img.setUserid(u.getId());
                images = imgService.selectimg(img);// 这是我们的sql
            }
        } else {
            img.setUserid(u.getId());
            images = imgService.selectimg(img);// 这是我们的sql
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
                de.delectUSS(key, images.getImgname());
            } else if (key.getStorageType() == 4) {
                de.delectKODO(key, images.getImgname());
            } else if (key.getStorageType() == 5) {
                LocUpdateImg.deleteLOCImg(images.getImgname());
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
                    de.delectUSS(key, images.getImgname());
                } else if (key.getStorageType() == 4) {
                    //初始化七牛云
                    de.delectKODO(key, images.getImgname());
                } else if (key.getStorageType() == 5) {
                    LocUpdateImg.deleteLOCImg(images.getImgname());
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

    //进入修改密码页面
    @RequestMapping(value = "/tosetuser")
    public String tosetuser(HttpSession session, Model model, HttpServletRequest request) {
        User u = (User) session.getAttribute("user");
        //key信息
        model.addAttribute("username", u.getUsername());
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


    @GetMapping(value = "/images/{id}")
    @ResponseBody
    public Images selectByFy(@PathVariable("id") Integer id) {
        return imgService.selectByPrimaryKey(id);
    }


}
