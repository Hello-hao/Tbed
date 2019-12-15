package cn.hellohao.controller;

import cn.hellohao.pojo.Code;
import cn.hellohao.pojo.Config;
import cn.hellohao.pojo.User;
import cn.hellohao.service.CodeService;
import cn.hellohao.service.KeysService;
import cn.hellohao.service.UserService;
import cn.hellohao.service.impl.ImgServiceImpl;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-08-11 14:25
 */
@Controller
@RequestMapping("/admin/root")
public class CodeController {
    @Autowired
    private CodeService codeService;


    @RequestMapping(value = "/tocode")
    public String tocode() {

        return "admin/code";
    }

    @RequestMapping(value = "/selectcodelist")
    @ResponseBody
    public Map<String, Object> selectcodel(HttpSession session, @RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false) int limit) {
        User u = (User) session.getAttribute("user");
        PageHelper.startPage(page, limit);
        List<Code> codes = null;
        if (u.getLevel() > 1) {
            codes = codeService.selectCode(null);
            // 使用pageInfo包装查询
            PageInfo<Code> rolePageInfo = new PageInfo<>(codes);
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

    @RequestMapping("/deletecodes")
    @ResponseBody
    public Integer deletecodes(HttpSession session, @RequestParam("arr[]") String[] arr){
        Integer v = 0;
        User u = (User) session.getAttribute("user");
        for (int i = 0; i < arr.length; i++) {
           v =  codeService.deleteCode(arr[i]);
        }
        return v;
    }
    @RequestMapping("/deletecode")
    @ResponseBody
    public Integer deletecode(String code){
            Integer v =  codeService.deleteCode(code);
        return v;
    }

    @RequestMapping("/addcode")
    @ResponseBody
    public Integer addcode(Integer value,Integer counts){
        Integer val = 0;
        Code code = new Code();
        for (int i= 0;i<counts;i++) {
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("hhmmss");
            Integer number = (int)(Math.random()*100000)+1;
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);
            code.setValue(value);
            code.setCode(SecureUtil.sha256(number+format1.format(new Date())+uuid));
            codeService.addCode(code);
            val = 1;
        }
        return val;
    }

}
