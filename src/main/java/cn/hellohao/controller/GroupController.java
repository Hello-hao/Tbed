package cn.hellohao.controller;

import cn.hellohao.pojo.Group;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.User;
import cn.hellohao.service.GroupService;
import cn.hellohao.service.KeysService;
import cn.hellohao.service.UserService;
import cn.hellohao.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/19 16:35
 */
@Controller
@RequestMapping("/admin/root")
public class GroupController {
    @Autowired
    private GroupService groupService;
    @Autowired
    private KeysService keysService;
    @Autowired
    private UserService userService;


    @RequestMapping(value = "/group")
    public String togroup() {
        return "admin/group";
    }
    @RequestMapping(value = "/addgroup")
    public String addgroup() {
        return "admin/addgroup";
    }
    //获取code列表
    @RequestMapping(value = "/getgrouplist")
    @ResponseBody
    public Map<String, Object> getgrouplist(HttpSession session, @RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false) int limit) {
        User u = (User) session.getAttribute("user");
        PageHelper.startPage(page, limit);
        List<Group> group = null;
        if (u.getLevel() > 1) {
            group = groupService.grouplist();
            PageInfo<Group> rolePageInfo = new PageInfo<>(group);
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

    @RequestMapping(value = "/addisgroup")
    @ResponseBody
    public Integer addisgroup(Group group) {
        boolean b =false;
        if(group.getKeyid()==5){
            b =true;
        }else{
            Keys key = keysService.selectKeys(group.getKeyid());
            b = StringUtils.doNull(group.getKeyid(),key);
        }
        Integer ret = 0;
        if(b){
            ret = groupService.addgroup(group);
        }else{
            ret = -1;
        }
        return ret;
    }
    @RequestMapping(value = "/delegroup")
    @ResponseBody
    public Integer delegroup(Integer id) {
        Integer ret = -1;
        if(id!=1){
            ret = groupService.delegroup(id);
        }
        return ret;
    }

    @PostMapping("/getgrouplist")
    @ResponseBody
    public String getgrouplist() {
        List<Group> li = groupService.grouplist();
        JSONArray jsonArray = new JSONArray();
        for (Group group : li) {
            jsonArray.add(group);
        }
        return jsonArray.toString();
    }

    @PostMapping("/updateuser")
    @ResponseBody
    public Integer updateuser(User user) {
        Integer ret = userService.change(user);
        return ret;
    }

    @PostMapping("/updategroup")
    @ResponseBody
    public Integer updategroup(Group group) {
        Integer ret = groupService.setgroup(group);
        return ret;
    }

    @RequestMapping("/modifygroup")
    public String modifygroup(Model model, Integer id) {
        Group group = groupService.idgrouplist(id);
        model.addAttribute("group",group);
        return "admin/setgroup";
    }



}
