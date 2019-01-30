package cn.hellohao.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONArray;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.User;
import cn.hellohao.service.KeysService;
import cn.hellohao.service.UserService;
import cn.hellohao.service.impl.NOSImageupload;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Controller
public class UpdateImgController {
	@Autowired
	private NOSImageupload nOSImageupload;
	@Autowired
	private UserService userService;
	@Autowired
	private KeysService keysService;
	
	
	public static String getSubString(String text, String left, String right) {
		String result = "";
		int zLen;
		if (left == null || left.isEmpty()) {
			zLen = 0;
		} else {
			zLen = text.indexOf(left);
			if (zLen > -1) {
				zLen += left.length();
			} else {
				zLen = 0;
			}
		}
		int yLen = text.indexOf(right, zLen);
		if (yLen < 0 || right == null || right.isEmpty()) {
			yLen = text.length();
		}
		result = text.substring(zLen, yLen);
		return result;
	}
	
	
	
    @RequestMapping({"/","/index.do"})
    public String indexImg(Model model,HttpServletRequest request,HttpSession httpSession){
		Integer storageType = keysService.selectKeysType();
		System.out.println("类型=========="+storageType);
		Keys key = keysService.selectKeys(storageType);
		nOSImageupload.Initialize(key);
    	//查询哪种对象存储
    	httpSession.setAttribute("storageType", storageType);
    	
    	model.addAttribute("text_foot", "<li><a href=\"http://www.hellohao.cn/\" target=\"_blank\">Hellohao  &copy;</a></li><li>切勿上传违反中华人民共和国互联网法律条约资源</li>");
    	User u = (User) httpSession.getAttribute("user");
    	String email = (String) httpSession.getAttribute("email");
    	String pass = (String) httpSession.getAttribute("pass");
    	if(email!=null && pass!=null) {
    		//登陆成功
    		Integer ret = userService.login(u.getEmail(), u.getPassword());
    		if(ret>0) {
    			User user = userService.getUsers(u.getEmail());
    			model.addAttribute("username",user.getUsername());
    			model.addAttribute("level",user.getLevel());
    			model.addAttribute("loginid",100);
    			model.addAttribute("imgcount",3);
    			model.addAttribute("fileSize",5120);
    			
    		}else {
    			model.addAttribute("loginid",-1);
    			model.addAttribute("imgcount",1);
    		}
    	}else {
    		model.addAttribute("loginid",-2);
    		model.addAttribute("imgcount",1);
    		model.addAttribute("imgcount",1);
    		model.addAttribute("fileSize",3072);
    		
    	}
        return "index";

    }
    
    @RequestMapping(value="/upimg.do")
    @ResponseBody
    public String exit(HttpServletRequest request,HttpServletResponse response,HttpSession session
            ,@RequestParam(value = "file", required = false) MultipartFile[] file,String filename) throws Exception{
		long stime = System.currentTimeMillis();
    	User u = (User) session.getAttribute("user");


    	JSONArray jsonArray = new JSONArray();
    	Map<String, MultipartFile> map = new HashMap<>();
        for (MultipartFile multipartFile : file) {
        	//获取文件名
            String fileName = multipartFile.getOriginalFilename();
            String lastname = fileName.substring(fileName.lastIndexOf(".") + 1);
	        if (!multipartFile.isEmpty()) { //判断文件是否为空 
	        	map.put(lastname, multipartFile);
	            //multipartFile.getOriginalFilename();  //文件名
	            //multipartFile.getSize();  //文件大小
	        }
        }
        Map<String, Integer> m =nOSImageupload.Imageupload(map);
    	Images img = new Images();
//    	for (String string : m) {
//    		jsonArray.add(string);
//    		System.out.println("文件名字：==="+string);
//    		if(userid!=null) {
//	    		//img.setImgname(imgname);//图片名字
//	    		img.setImgurl(string);//图片链接
//	    		img.setUserid(userid);//用户id
//	    		userService.insertimg(img);
//    		}
//    		
//		}
    	
		for (Map.Entry<String, Integer> entry : m.entrySet()) {
    		jsonArray.add(entry.getKey());
    		if(u!=null) {
	    		//img.setImgname(imgname);//图片名字
    			
	    		img.setImgurl(entry.getKey());//图片链接
	    		img.setUserid(u.getId());//用户id
	    		img.setSizes((entry.getValue())/1024);
	    		img.setImgname(getSubString(entry.getKey(), "hellohao.nos-eastchina1.126.net/", ""));
	    		userService.insertimg(img);
	    		long etime = System.currentTimeMillis();
				System.out.println("上传图片所用时长："+String.valueOf(etime-stime)+"ms");
    		}
		}
    	
    	return jsonArray.toString();
    }



}
