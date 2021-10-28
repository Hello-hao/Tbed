package cn.hellohao.controller;

import cn.hellohao.pojo.Imgreview;
import cn.hellohao.pojo.Msg;
import cn.hellohao.service.ImgreviewService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin/root")
public class ImageReviewController {

    @Autowired
    private ImgreviewService imgreviewService;


    @PostMapping("/updateimgReviewConfig") //new
    @ResponseBody
    public Msg updateimgReviewConfig(@RequestParam(value = "data", defaultValue = "") String data ) {
        final Msg msg = new Msg();
        try {
            JSONObject jsonObj = JSONObject.parseObject(data);
            Imgreview imgreview = JSON.toJavaObject(jsonObj,Imgreview.class);
            if(imgreview.getId()==1){
                if(null==imgreview.getId() || null==imgreview.getApiKey() || null==imgreview.getUsing() || null==imgreview.getSecretKey()
                        || null==imgreview.getAppId() || imgreview.getApiKey().equals("")
                        || imgreview.getSecretKey().equals("") || imgreview.getAppId().equals("")){
                    msg.setCode("110400");
                    msg.setInfo("各参数不能为空");
                    return msg;
                }
            }else{
                if(null==imgreview.getId() || null==imgreview.getApiKey() || null==imgreview.getUsing()
                        || imgreview.getApiKey().equals("")
                ){
                    msg.setCode("110400");
                    msg.setInfo("各参数不能为空");
                    return msg;
                }
            }
            if(null == imgreviewService.selectByPrimaryKey(imgreview.getId())){
                imgreviewService.insert(imgreview);
            }else{
                imgreviewService.updateByPrimaryKeySelective(imgreview);
            }
            msg.setInfo("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("保存过程出现错误");
        }
        return msg;
    }

}
