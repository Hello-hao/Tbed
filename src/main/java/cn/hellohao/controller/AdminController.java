package cn.hellohao.controller;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.User;
import cn.hellohao.pojo.vo.PageResultBean;
import cn.hellohao.service.ImgService;
import cn.hellohao.service.KeysService;
import cn.hellohao.service.UserService;
import cn.hellohao.service.impl.ImgServiceImpl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ImgService imgService;
	@Autowired
	private KeysService keysService;
	@Autowired
	private UserService userService;

	// 进入后台页面
	@RequestMapping(value = "/admin.do")
	public String goadmin(HttpSession session, Model model, HttpServletRequest request) {
		Integer storageType1 = (Integer) session.getAttribute("storageType");
		Integer storageType = keysService.selectKeysType();
		Keys key = keysService.selectKeys(storageType);
		User u = (User) session.getAttribute("user");
		if(u.getLevel()>1) {
			model.addAttribute("counts", imgService.counts(null));
			model.addAttribute("getUserTotal", userService.getUserTotal());
		}else {
			model.addAttribute("counts", imgService.countimg(u.getId()));
		}
		model.addAttribute("username", u.getUsername());
		model.addAttribute("level", u.getLevel());
		model.addAttribute("email", u.getEmail());
		model.addAttribute("loginid", 100);
		//key信息
		model.addAttribute("AccessKey", key.getAccessKey());
		model.addAttribute("AccessSecret", key.getAccessSecret());
		model.addAttribute("Endpoint", key.getEndpoint());
		model.addAttribute("Bucketname", key.getBucketname());
		model.addAttribute("RequestAddress", key.getRequestAddress());
		model.addAttribute("StorageType", storageType);
		model.addAttribute("text_foot", "<li><a href=\"http://www.hellohao.cn/\" target=\"_blank\">Hellohao  &copy;</a></li><li>切勿上传违反中华人民共和国互联网法律条约资源</li>");
		if (u.getLevel() > 1) {
			model.addAttribute("htgl",
					"<input id=\"isadmin\" onclick=\"openLoginModal();\" class=\"btn btn-default\" type=\"button\" value=\"后台管理\">");
		}
		return "admin/table";
	}

	@RequestMapping(value = "/selecttable.do")
	@ResponseBody
	public PageResultBean<Images> selectByFy(HttpSession session, Integer pageNum, Integer pageSize) {
		User u = (User) session.getAttribute("user");
		// 使用Pagehelper传入当前页数和页面显示条数会自动为我们的select语句加上limit查询
		// 从他的下一条sql开始分页
		PageHelper.startPage(pageNum, pageSize);
		List<Images> images = null;
		if(u.getLevel()>1){ //根据用户等级查询管理员查询所有的信息
			images = imgService.selectimg(null);// 这是我们的sql
		}else{
			images = imgService.selectimg(u.getId());// 这是我们的sql
		}
		// 使用pageInfo包装查询
		PageInfo<Images> rolePageInfo = new PageInfo<>(images);//
		return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
	}
	
	@PostMapping("/updatekey.do")
	@ResponseBody
	public String updatekey(Keys key) {
		Integer ret = keysService.updateKey(key);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(ret);
		return jsonArray.toString();
	}
	@PostMapping("/deleimg.do")
	@ResponseBody
	public String deleimg(HttpSession session,Integer id) {
		Integer storageType = (Integer) session.getAttribute("storageType");
		Images images = imgService.selectByPrimaryKey(id);
		Keys key = keysService.selectKeys(storageType);
		ImgServiceImpl de = new ImgServiceImpl();
		de.delect(key, images.getImgname());
		JSONArray jsonArray = new JSONArray();
		Integer ret = imgService.deleimg(id);	
		jsonArray.add(ret);
		return jsonArray.toString();
	}
	
	//修改资料
	@PostMapping("/change.do")
	@ResponseBody
	public String change(HttpSession session,User user) {
		System.out.println(user.getUsername());
		Integer count = userService.checkUsername(user.getUsername());
		User u = (User) session.getAttribute("user");
		user.setEmail(u.getEmail());
		JSONArray jsonArray = new JSONArray();
		if(count==0) {
			Integer ret = userService.change(user);
			jsonArray.add(ret);
			if(u.getEmail()!=null&&u.getPassword()!=null) {
	    		session.removeAttribute("user");
	          //刷新view
	            session.invalidate();
	    	}
			
		}else {
			jsonArray.add("-1");
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
