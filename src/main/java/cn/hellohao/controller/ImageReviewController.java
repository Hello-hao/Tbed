package cn.hellohao.controller;

import cn.hellohao.pojo.Imgreview;
import cn.hellohao.service.ImgreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class ImageReviewController {

    @Autowired
    private ImgreviewService imgreviewService;


    @RequestMapping(value = "/root/ImageReview")
    public String ForwardImageReview(Model model) {
        Imgreview imgreview = imgreviewService.selectByPrimaryKey(1);
        model.addAttribute("appid", imgreview.getAppId());
        model.addAttribute("apikey", imgreview.getApiKey());
        model.addAttribute("secretkey", imgreview.getSecretKey());
        model.addAttribute("using", imgreview.getUsing());
        return "admin/imageIdentify";
    }

    @RequestMapping(value = "/root/ImgreviewSwitch")
    @ResponseBody
    public String ImgreviewSwitch(String appId, String apiKey, String secretKey, Integer using) {
        Imgreview imgreview = new Imgreview();
        imgreview.setId(1);//目前就一种鉴黄功能所以设死了
        imgreview.setUsing(using);
        imgreview.setAppId(appId);
        imgreview.setApiKey(apiKey);
        imgreview.setSecretKey(secretKey);
        Integer ret = imgreviewService.updateByPrimaryKeySelective(imgreview);

        return ret.toString();
    }

}
