package cn.hellohao.controller;

import cn.hellohao.pojo.Code;
import cn.hellohao.pojo.Msg;
import cn.hellohao.service.CodeService;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping(value = "/selectCodeList") 
    @ResponseBody
    public Msg selectCodeList(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        Integer pageNum = jsonObj.getInteger("pageNum");
        Integer pageSize = jsonObj.getInteger("pageSize");
        PageHelper.startPage(pageNum, pageSize);
        List<Code> codes = null;
        try {
            codes = codeService.selectCode(null);
            PageInfo<Code> rolePageInfo = new PageInfo<>(codes);
            msg.setData(rolePageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("500");
            msg.setInfo("获取数据异常");
        }
        return msg;
    }

    @RequestMapping("/deleteCodes") 
    @ResponseBody
    public Msg deletecodes(@RequestParam(value = "data", defaultValue = "") String data){
        final Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        JSONArray arr = jsonObj.getJSONArray("arr");
        Integer v = 0;
        try {
            for (int i = 0; i < arr.size(); i++) {
                codeService.deleteCode(arr.getJSONObject(i).getString("code"));
                v++;
            }
            msg.setInfo("已成功删除"+v+"个扩容码");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("删除过程中发生现错误，已成功删除"+v+"个");
        }
        return msg;
    }


    @RequestMapping("/addCode") 
    @ResponseBody
    public Msg addcode(@RequestParam(value = "data", defaultValue = "") String data){
        final Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        Long value = jsonObj.getLong("memory");
        Long count = jsonObj.getLong("count");
        if(value<=0 || value>1048576 || count<=0 || count>1000){
            msg.setInfo("数据格式错误,请正确输入");
            return msg;
        }
        Integer val = 0;
        Code code = new Code();
        for (int i= 0;i<count;i++) {
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("hhmmss");
            Integer number = (int)(Math.random()*100000)+1;
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase().substring(0,5);
            code.setValue(Long.toString(value*1024*1024));
            code.setCode(SecureUtil.sha256(number+format1.format(new Date())+uuid));
            codeService.addCode(code);
            val++;
        }
        msg.setInfo("已成功生成"+val+"个扩容码");
        return msg;
    }





}
