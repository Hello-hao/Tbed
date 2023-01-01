package cn.hellohao.controller;

import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.pojo.*;
import cn.hellohao.service.*;
import cn.hellohao.service.impl.AlbumServiceImpl;
import cn.hellohao.service.impl.UploadServicel;
import cn.hellohao.service.impl.deleImages;
import cn.hellohao.utils.GetIPS;
import cn.hellohao.utils.MyVersion;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
public class IndexController {
    @Autowired private ImgService imgService;
    @Autowired private SysConfigService sysConfigService;
    @Autowired private ConfigService configService;
    @Autowired private UploadConfigService uploadConfigService;
    @Autowired private UploadServicel uploadServicel;
    @Autowired private deleImages deleimages;
    @Autowired private IRedisService iRedisService;
    @Autowired private AppClientService appClientService;

    @RequestMapping(value = "/")
    public String Welcome(Model model, HttpServletRequest httpServletRequest) {
        model.addAttribute("name", "服务端程序");
        model.addAttribute("version", "20230101");
        model.addAttribute("ip", GetIPS.getIpAddr(httpServletRequest));
        model.addAttribute("links", "tbed.hellohao.cn");
        return "welcome";
    }

    @RequestMapping(value = "/webInfo")
    @ResponseBody
    public Msg webInfo() {
        Msg msg = new Msg();
        Config config = configService.getSourceype();
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        SysConfig sysConfig = sysConfigService.getstate();
        JSONObject jsonObject = new JSONObject();
        AppClient appClient = appClientService.getAppClientData("app");
        jsonObject.put("webname", config.getWebname());
        jsonObject.put("websubtitle", config.getWebsubtitle());
        jsonObject.put("keywords", config.getWebkeywords());
        jsonObject.put("description", config.getWebms());
        jsonObject.put("explain", config.getExplain());
        jsonObject.put("favicon", config.getWebfavicons());
        jsonObject.put("baidu", config.getBaidu());
        jsonObject.put("links", config.getLinks());
        jsonObject.put("aboutinfo", config.getAboutinfo());
        jsonObject.put("logo", config.getLogo());
        jsonObject.put("api", updateConfig.getApi());
        jsonObject.put("register", sysConfig.getRegister());
        jsonObject.put("isuse", appClient.getIsuse());
        jsonObject.put("clientname", appClient.getAppname());
        jsonObject.put("clientlogo", appClient.getApplogo());
        jsonObject.put("appupdate", appClient.getAppupdate());
        msg.setData(jsonObject);
        return msg;
    }

    @PostMapping(value = {"/upload", "/client/upload"})
    @ResponseBody
    public Msg upimg(
            HttpServletRequest request,
            @RequestParam(value = "file", required = false) MultipartFile multipartFile,
            Integer day) {
        return uploadServicel.uploadForLoc(request, multipartFile, day, null);
    }

    @PostMapping(value = {"/uploadForUrl", "/client/uploadForUrl"})
    @ResponseBody
    public Msg upurlimg(
            HttpServletRequest request, @RequestParam(value = "data", defaultValue = "") String data) {
        JSONObject jsonObj = JSONObject.parseObject(data);
        Integer setday = jsonObj.getInteger("day");
        String imgUrl = jsonObj.getString("imgUrl");
        String[] URLArr = imgUrl.split("\n");

        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
        int syscounts = 0;
        if (null == user) {
            syscounts = updateConfig.getImgcounttourists();
        } else {
            syscounts = updateConfig.getImgcountuser();
        }
        Msg retMsg = new Msg();
        JSONObject jsonObject = new JSONObject();
        JSONArray retArray = new JSONArray();
        int errcounts = 0;
        int excess = 0;
        for (int i = 0; i < URLArr.length; i++) {
            int temp = i + 1;
            if (syscounts >= temp) {
                Msg msg = uploadServicel.uploadForLoc(request, null, setday, URLArr[i]);
                if (!msg.getCode().equals("200")) {
                    errcounts++;
                } else {
                    retArray.add(msg);
                }
            } else {
                excess++;
            }
        }
        jsonObject.put("counts", URLArr.length);
        jsonObject.put("errcounts", errcounts);
        jsonObject.put("excess", excess);
        jsonObject.put("urls", retArray);
        retMsg.setData(jsonObject);
        return retMsg;
    }

    @RequestMapping(value = "/getUploadInfo")
    @ResponseBody
    public Msg getUploadInfo() {
        Msg msg = new Msg();
        JSONObject jsonObject = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        try {
            UploadConfig updateConfig = uploadConfigService.getUpdateConfig();
            jsonObject.put("suffix", updateConfig.getSuffix().split(","));
            if (null == user) {
                jsonObject.put("filesize", Integer.valueOf(updateConfig.getFilesizetourists()) / 1024);
                jsonObject.put("imgcount", updateConfig.getImgcounttourists());
                jsonObject.put("uploadSwitch", updateConfig.getIsupdate());
                jsonObject.put("uploadInfo", "您登陆后才能使用此功能哦");
            } else {
                jsonObject.put("filesize", Integer.valueOf(updateConfig.getFilesizeuser()) / 1024);
                jsonObject.put("imgcount", updateConfig.getImgcountuser());
                jsonObject.put("uploadSwitch", updateConfig.getUserclose());
                jsonObject.put("uploadInfo", "系统暂时关闭了用户上传功能");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        msg.setData(jsonObject);
        return msg;
    }

    @RequestMapping("/checkStatus")
    @ResponseBody
    public Msg checkStatus(HttpServletRequest request) {
        Msg msg = new Msg();
        String token = request.getHeader("Authorization");
        if (token != null) {
            JSONObject tokenJson = JWTUtil.checkToken(token);
            if (tokenJson.getBoolean("check")) {
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken tokenOBJ =
                        new UsernamePasswordToken(
                                tokenJson.getString("email"), tokenJson.getString("password"));
                tokenOBJ.setRememberMe(true);
                try {
                    subject.login(tokenOBJ);
                    SecurityUtils.getSubject().getSession().setTimeout(3600000);
                    User u = (User) subject.getPrincipal();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("RoleLevel", u.getLevel() == 2 ? "admin" : "user");
                    jsonObject.put("userName", u.getUsername());
                    msg.setCode("200");
                    msg.setData(jsonObject);
                } catch (Exception e) {
                    msg.setCode("40041");
                    msg.setInfo("登录失效，请重新登录");
                    System.err.println("登录失效，请重新登录");
                    e.printStackTrace();
                }
            } else {
                msg.setCode("40041");
                msg.setInfo("登录失效，请重新登录");
            }
        } else {
            msg.setCode("40040");
            msg.setInfo("当前未登录，请先登录");
        }
        return msg;
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public Msg verifyCode() {
        Msg msg = new Msg();
        try {
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(90, 35, 2, 2);
            captcha.setGenerator(new MathGenerator(1));
            String code = getVerifyCodeOperator(captcha.getCode());
            String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            iRedisService.setValue("verifyCode_" + uid, code);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("codeKey", uid);
            jsonObject.put("codeImg", captcha.getImageBase64());
            msg.setData(jsonObject);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("500");
            return msg;
        }
    }

    @PostMapping("/verifyCodeForRegister")
    @ResponseBody
    public Msg verifyCodeForRegister() {
        Msg msg = new Msg();
        try {
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(90, 35, 2, 2);
            captcha.setGenerator(new MathGenerator(1));
            String code = getVerifyCodeOperator(captcha.getCode());
            String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            iRedisService.setValue("verifyCodeForRegister_" + uid, code);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("codeKey", uid);
            jsonObject.put("codeImg", captcha.getImageBase64());
            msg.setData(jsonObject);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("500");
            return msg;
        }
    }

    @PostMapping("/verifyCodeForRetrieve")
    @ResponseBody
    public Msg verifyCodeForRetrieve() {
        Msg msg = new Msg();
        try {
            ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(90, 35, 2, 2);
            captcha.setGenerator(new MathGenerator(1));
            String code = getVerifyCodeOperator(captcha.getCode());
            String uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            iRedisService.setValue("verifyCodeForRetrieve_" + uid, code);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("codeKey", uid);
            jsonObject.put("codeImg", captcha.getImageBase64());
            msg.setData(jsonObject);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            msg.setCode("500");
            return msg;
        }
    }

    public static String getVerifyCodeOperator(String str) {
        int a = Integer.valueOf(str.substring(0, 1));
        String yxf = str.substring(1, 2);
        int b = Integer.valueOf(str.substring(2, 3));
        switch (yxf) {
            case "*":
                return Integer.toString(a * b);
            case "+":
                return Integer.toString(a + b);
            case ",":
            default:
                return Integer.toString(82);
            case "-":
                return Integer.toString(a - b);
        }
    }

    @PostMapping({"/deleImagesByUid", "/client/deleImagesByUid"})
    @ResponseBody
    public Msg deleImagesByUid(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        JSONObject jsonObj = JSONObject.parseObject(data);
        String imguid = jsonObj.getString("imguid");
        Images image = imgService.selectImgUrlByImgUID(imguid);
        if (null == image) {
            msg.setInfo("图像已不存在，删除成功");
            return msg;
        }
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        if (null != user) {
            if (!user.getId().equals(image.getUserid())) {
                msg.setInfo("删除失败，该图片不允许你执行操作");
                msg.setCode("100403");
                return msg;
            }
        }
        msg = deleimages.dele(null, image.getId());
        List<Long> Delids = (List<Long>) msg.getData();
        if (!Delids.contains(image.getId())) {
            msg.setCode("500");
            msg.setInfo("图像删除失败");
        } else {
            msg.setCode("200");
            msg.setInfo("图像已成功删除");
        }
        return msg;
    }

    @PostMapping("/getClientVersion")
    @ResponseBody
    public Msg getClientVersion(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        msg.setCode("000");
        try {
            JSONObject jsonObj = JSONObject.parseObject(data);
            String appv = jsonObj.getString("version");
            AppClient app = appClientService.getAppClientData("app");
            if (!StringUtils.isBlank(app.getAppupdate())) {
                String version = app.getAppupdate().replaceAll("\\.", "");
                if (MyVersion.compareVersion(app.getAppupdate(), appv) == 1) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("winpackurl", app.getWinpackurl());
                    jsonObject.put("macpackurl", app.getMacpackurl());
                    msg.setCode("200");
                    msg.setData(jsonObject);
                }
            }
        } catch (Exception e) {
            msg.setCode("000");
        }
        return msg;
    }

    @RequestMapping("/authError")
    @ResponseBody
    public Msg authError(HttpServletRequest request) {
        Msg msg = new Msg();
        msg.setCode("4031");
        msg.setInfo("You don't have authority");
        return msg;
    }

    @RequestMapping("/jurisError")
    @ResponseBody
    public Msg jurisError(HttpServletRequest request) {
        Msg msg = new Msg();
        msg.setCode("4031");
        msg.setInfo("Authentication request failed");
        return msg;
    }
}
