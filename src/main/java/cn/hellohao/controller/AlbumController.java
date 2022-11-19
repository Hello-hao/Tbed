package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.pojo.vo.PageResultBean;
import cn.hellohao.service.ImgAndAlbumService;
import cn.hellohao.service.UserService;
import cn.hellohao.service.impl.AlbumServiceImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/12/17 11:25
 */
@Controller
public class AlbumController {
    @Autowired
    AlbumServiceImpl albumServiceImpl;
    @Autowired
    ImgAndAlbumService imgAndAlbumService;
    @Autowired
    private UserService userService;

    @PostMapping("/admin/getGalleryList")  
    @ResponseBody
    public Msg getGalleryList (@RequestParam(value = "data", defaultValue = "") String data){
        Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        user =  userService.getUsers(user);
        Album album = new Album();
        JSONObject jsonObj = JSONObject.parseObject(data);
        Integer pageNum = jsonObj.getInteger("pageNum");
        Integer pageSize = jsonObj.getInteger("pageSize");
        Integer albumtitle = jsonObj.getInteger("albumtitle");
        if(subject.hasRole("admin")){
        }else{
            album.setUserid(user.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Album> albums = null;
        try {
            albums = albumServiceImpl.selectAlbumURLList(album);
            PageInfo<Album> rolePageInfo = new PageInfo<>(albums);
            msg.setData(rolePageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setInfo("获取数据异常");
            msg.setCode("500");
        }
        return msg;

    }

    @PostMapping("/admin/deleGallery")
    @ResponseBody
    public Msg deleGallery (@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            JSONArray albumkeyList = jsonObject.getJSONArray("albumkeyList");
            for (int i = 0; i < albumkeyList.size(); i++) {
                if(subject.hasRole("admin")){
                    albumServiceImpl.deleteAlbum(albumkeyList.getString(i));
                }else{
                    Album album = new Album();
                    album.setAlbumkey(albumkeyList.getString(i));
                    final Album alb = albumServiceImpl.selectAlbum(album);
                    if(alb.getUserid()==user.getId()){
                        albumServiceImpl.deleteAlbum(albumkeyList.getString(i));
                    }
                }
            }
            msg.setInfo("画廊已成功删除");
        }catch (Exception e){
            msg.setCode("500");
            msg.setInfo("画廊删除失败");
            e.printStackTrace();
        }
        return msg;

    }

    //后台列表要修改时的查询表单
    @PostMapping("/admin/getAlbumListForKey")  
    @ResponseBody
    public Msg getAlbumListForKey(@RequestParam(value = "data", defaultValue = "") String data){
        Msg msg = new Msg();
        try{
            JSONObject jsonObject = JSONObject.parseObject(data);
            String key = jsonObject.getString("key");
            Subject subject = SecurityUtils.getSubject();
            User user = (User) subject.getPrincipal();
            Album album = new Album();
            album.setAlbumkey(key);
            if(subject.hasRole("admin")){
                album.setUserid(null);
            }else{
                album.setUserid(user.getId());
            }
            JSONObject jsonObj = new JSONObject();
            Album album1 = albumServiceImpl.selectAlbum(album);
            List<Images> imagesList =  imgAndAlbumService.selectImgForAlbumkey(key);
            jsonObj.put("album",album1);
            jsonObj.put("imagesList",imagesList);
            msg.setData(jsonObj);
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("500");
            msg.setInfo("获取画廊数据错误");
        }
        return msg;

    }

    @PostMapping("/getAlbumImgList")  
    @ResponseBody
    public Msg getAlbumImgList(@RequestParam("data") String data) {
        Msg msg = new Msg();
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("imguidlist");
        JSONArray json = albumServiceImpl.getAlbumList(jsonArray);
        msg.setData(json);
        return msg;
    }

    @PostMapping("/SaveForAlbum") 
    @ResponseBody
    public Msg SaveForAlbum(@RequestParam(value = "data", defaultValue = "") String data){
        data = StringEscapeUtils.unescapeHtml4(data);
        Msg msg = new Msg();
        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            String albumtitle = jsonObject.getString("albumtitle");
            String password = jsonObject.getString("password");
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("albumlist"));
            if(null!=password){
                password = password.replace(" ", "");
                if(password.replace(" ", "").equals("") || password.length()<3){
                    msg.setCode("110403");
                    msg.setInfo("密码长度不得小于三位有效字符");
                    return msg;
                }
            }
            if(albumtitle==null || jsonArray.size()==0){
                msg.setCode("110404");
                msg.setInfo("标题和图片参数不能为空");
                return msg;
            }
            Subject subject = SecurityUtils.getSubject();
            User u = (User) subject.getPrincipal();
            String uuid = "TOALBUM"+ UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,7)+"N";
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Album album = new Album();
            album.setAlbumtitle(albumtitle);
            album.setCreatedate(df.format(new Date()));
            album.setPassword(password);
            album.setAlbumkey(uuid);
            if(u==null){
                album.setUserid(0);
            }else{
                album.setUserid(u.getId());
            }
            albumServiceImpl.addAlbum(album);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject img = jsonArray.getJSONObject(i);
                ImgAndAlbum imgAndAlbum = new ImgAndAlbum();
                imgAndAlbum.setImgname(img.getString("imgurl"));
                imgAndAlbum.setAlbumkey(uuid);
                imgAndAlbum.setNotes(img.getString("notes"));
                albumServiceImpl.addAlbumForImgAndAlbumMapper(imgAndAlbum);
            }
            final JSONObject json = new JSONObject();
            json.put("url",uuid);
            json.put("title",albumtitle);
            json.put("password",password);
            msg.setCode("200");
            msg.setInfo("成功创建画廊链接");
            msg.setData(json);
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("500");
            msg.setInfo("创建画廊链接失败");
        }
        return msg;
    }


    @PostMapping("/admin/UpdateForAlbum") 
    @ResponseBody
    public Msg UpdateForAlbum(@RequestParam(value = "data", defaultValue = "") String data){
        data = StringEscapeUtils.unescapeHtml4(data);
        Msg msg = new Msg();
        try {
            JSONObject jsonObject = JSONObject.parseObject(data);
            String albumkey = jsonObject.getString("albumkey");
            String albumtitle = jsonObject.getString("albumtitle");
            String password = jsonObject.getString("password");
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("albumlist"));
            if(null!=password){
//                String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";
//                return str.matches(regex);
                password = password.replace(" ", "");
                if(password.replace(" ", "").equals("") || password.length()<3){
                    msg.setCode("110403");
                    msg.setInfo("密码长度不得小于三位有效字符");
                    return msg;
                }
            }
            if(albumtitle==null || jsonArray.size()==0){
                msg.setCode("110404");
                msg.setInfo("标题和图片参数不能为空");
                return msg;
            }
            Subject subject = SecurityUtils.getSubject();
            User u = (User) subject.getPrincipal();
            Album album = new Album();
            album.setAlbumtitle(albumtitle);
            album.setPassword(password);
            album.setAlbumkey(albumkey);

            albumServiceImpl.updateAlbum(album);
            imgAndAlbumService.deleteImgAndAlbumForKey(albumkey);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject img = jsonArray.getJSONObject(i);
                ImgAndAlbum imgAndAlbum = new ImgAndAlbum();
                imgAndAlbum.setImgname(img.getString("imgurl"));
                imgAndAlbum.setAlbumkey(albumkey);
                imgAndAlbum.setNotes(img.getString("notes"));
                albumServiceImpl.addAlbumForImgAndAlbumMapper(imgAndAlbum);
            }
            final JSONObject json = new JSONObject();
            json.put("url",albumkey);
            json.put("title",albumtitle);
            json.put("password",password);
            msg.setCode("200");
            msg.setInfo("画廊修改成功");
            msg.setData(json);
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("500");
            msg.setInfo("修改画廊失败");
        }
        return msg;
    }


    @PostMapping("/checkPass") 
    @ResponseBody
    public Msg checkPass(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        JSONObject json = new JSONObject();
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(data);
            String key = jsonObject.getString("key");
            Album album = new Album();
            album.setAlbumkey(key);
            Album a =  albumServiceImpl.selectAlbum(album);
            if(a!=null){
                json.put("album",a);
                json.put("exist",true);
                if(a.getPassword()!=null && !a.getPassword().equals("")){
                    json.put("passType",true);
                }else{
                    json.put("passType",false);
                }
                msg.setData(json);
            }else{
                json.put("exist",false);
                msg.setData(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("操作失败");
            msg.setData(json);
        }
        return msg;
    }

    @PostMapping("/getAlbumList")  
    @ResponseBody
    public Msg getAlbumList(@RequestParam(value = "data", defaultValue = "") String data){
        Msg msg = new Msg();
        JSONObject json = new JSONObject();
        JSONObject jsonObject = JSONObject.parseObject(data);
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");
        String albumkey = jsonObject.getString("albumkey");
        String password = jsonObject.getString("password");
        if(null!=password){
            password = password.replace(" ", "");
        }
        Album album = new Album();
        album.setAlbumkey(albumkey);
        Album a =  albumServiceImpl.selectAlbum(album);
        if(a==null){
            msg.setCode("110404");
            msg.setInfo("画廊地址不存在");
        }else{
            PageHelper.startPage(pageNum, pageSize);
            if(a.getPassword()==null || (a.getPassword().replace(" ", "")).equals("")){
                List<Images> imagesList = imgAndAlbumService.selectImgForAlbumkey(albumkey);
                PageInfo<Images> rolePageInfo = new PageInfo<>(imagesList);
                PageResultBean<Images> pageResultBean = new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
                json.put("imagesList",pageResultBean);
            }else{
                if(a.getPassword().equals(password)){
                    List<Images> imagesList =  imgAndAlbumService.selectImgForAlbumkey(albumkey);
                    PageInfo<Images> rolePageInfo = new PageInfo<>(imagesList);
                    PageResultBean<Images> pageResultBean = new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
                    json.put("imagesList",pageResultBean);
                }else{
                    msg.setCode("110403");
                    msg.setInfo("画廊密码错误");
                }
            }
            json.put("titlename",a.getAlbumtitle());
            msg.setData(json);
        }
        return msg;
    }



}
