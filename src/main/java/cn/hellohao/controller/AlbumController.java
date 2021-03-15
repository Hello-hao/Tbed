package cn.hellohao.controller;

import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.AlbumServiceI;
import cn.hellohao.service.impl.ImgAndAlbumServiceImpl;
import cn.hellohao.utils.GetCurrentSource;
import cn.hellohao.utils.Print;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    AlbumServiceI albumServiceI;
    @Autowired
    private ConfigService configService;
    @Autowired
    private ImgService imgService;
    @Autowired
    ImgAndAlbumService imgAndAlbumService;
    @Autowired
    private UploadConfigService uploadConfigService;

    @RequestMapping("/addalbum")
    public String addalbum(HttpSession session,Model model) {
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        User u = (User) session.getAttribute("user");
        Integer sourcekey = 0;
        if (u == null) {
            sourcekey = GetCurrentSource.GetSource(null);
        } else {
            sourcekey = GetCurrentSource.GetSource(u.getId());
        }
        model.addAttribute("urltype",updateConfig.getUrltype());
        model.addAttribute("sourcekey",sourcekey);
        return "album/addalbum";
    }

    @PostMapping("/SaveForAlbum")
    @ResponseBody
    public Msg SaveForAlbum(HttpSession session,@RequestParam("imgarr[]") String[] imgarr, @RequestParam("aboutarr[]") String[] aboutarr,
                               String albumtitle, String password){
        Msg msg = new Msg();
        User u = (User) session.getAttribute("user");
        String uuid = "TOALBUM"+ UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5)+"N";
        Integer ret =0;
        Integer temp = 0;
        Integer temp2 = 0;
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < imgarr.length; i++) {
            Album album = new Album();
            ImgAndAlbum imgAndAlbum = new ImgAndAlbum();
            album.setAlbumtitle(albumtitle);
            album.setCreatedate(df.format(new Date()));
            album.setPassword(password);
            album.setAlbumkey(uuid);
            if(u==null){
                album.setUserid(0);
            }else{
                album.setUserid(u.getId());
            }
            String imgurl = "";
            Integer sourcekey = 0;
            UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
            if(updateConfig.getUrltype()==2){
                imgurl = imgarr[i];
            }else{
                if (u == null) {
                    //sourcekey = GetCurrentSource.GetSource(null);
                    imgurl="tourist/"+imgarr[i];
                } else {
                    //sourcekey = GetCurrentSource.GetSource(u.getId());
                    imgurl=u.getUsername()+"/"+imgarr[i];
                }
            }

            imgAndAlbum.setImgname(imgarr[i]);
            imgAndAlbum.setAlbumkey(uuid);
            imgAndAlbum.setNotes(aboutarr[i]);
            if(temp==0){
                temp2 = albumServiceI.addAlbum(album);
                temp = 1;
            }
            if(temp2>0){
                ret = albumServiceI.addAlbumForImgAndAlbumMapper(imgAndAlbum);
            }
        }
        if(ret>0){
            msg.setCode("200");
            msg.setInfo("成功创建画廊链接：");
            msg.setData(configService.getSourceype().getDomain()+"/"+uuid);
        }else{
            msg.setCode("500");
            msg.setInfo("创建画廊链接失败：");
        }
        return msg;
    }

    @RequestMapping("/TOALBUM{key}N")
    public String selectByFy(@PathVariable("key") String key, Model model) {
        Album album = new Album();
        album.setAlbumkey("TOALBUM"+key+"N");
        Album a =  albumServiceI.selectAlbum(album);
        Config config = configService.getSourceype();
        model.addAttribute("webname",config.getWebname());
        model.addAttribute("domain",config.getDomain());
        model.addAttribute("ico",config.getWebfavicons());
        if(a!=null){
            model.addAttribute("obj",a);
            model.addAttribute("album",1);
            if(a.getPassword()!=null && !a.getPassword().equals("")){
                model.addAttribute("passType","1");
            }else{
                model.addAttribute("passType","0");
            }
        }else{
            model.addAttribute("album",0);
        }
        return "album/album_new";
    }

    @PostMapping("/getAlbumList")
    @ResponseBody
    public Msg getAlbumList(String albumkey,String password){
        Msg msg = new Msg();
        Album album = new Album();
        album.setAlbumkey(albumkey);
        Album a =  albumServiceI.selectAlbum(album);
        if(a==null){
            msg.setCode("404");
            msg.setInfo("画廊地址不存在");
            return msg;
        }
        Config config = configService.getSourceype();
        JSONObject jsonObject = new JSONObject();
        if(a!=null){
            if(a.getPassword()==null){
                List<Images> imagesList =  imgAndAlbumService.selectImgForAlbumkey(albumkey);
                msg.setCode("200");
                jsonObject.put("imagesList",imagesList);
            }else{
                if(a.getPassword().equals(password)){
                    List<Images> imagesList =  imgAndAlbumService.selectImgForAlbumkey(albumkey);
                    msg.setCode("200");
                    jsonObject.put("imagesList",imagesList);
                }else{
                    msg.setCode("403");
                    msg.setInfo("画廊密码错误");
                }
            }
            jsonObject.put("titlename",a.getAlbumtitle());
        }
        jsonObject.put("webname",config.getWebname());
        msg.setData(jsonObject);
        return msg;
    }


    @RequestMapping("/addalbum_new")
    public String addalbum2() {
        return "album/album_new";
    }

}
