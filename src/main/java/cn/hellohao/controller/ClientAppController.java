package cn.hellohao.controller;

import cn.hellohao.auth.token.JWTUtil;
import cn.hellohao.pojo.Msg;
import cn.hellohao.pojo.UploadConfig;
import cn.hellohao.pojo.User;
import cn.hellohao.service.AppClientService;
import cn.hellohao.service.ImgService;
import cn.hellohao.service.UploadConfigService;
import cn.hellohao.service.impl.UploadServicel;
import cn.hellohao.service.impl.UserServiceImpl;
import cn.hellohao.utils.Base64ToMultipartFile;
import cn.hellohao.utils.SetFiles;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2022-05-16 32:45
 */
@RestController
@RequestMapping("/client")
public class ClientAppController {

    @Autowired
    private ImgService imgService;
    @Autowired
    private AppClientService appClientService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UploadConfigService uploadConfigService;
    @Autowired
    private UploadServicel uploadServicel;

    @PostMapping("/loginByToken")
    @ResponseBody
    public Msg loginByToken(@RequestParam(value = "data", defaultValue = "") String data) {
        Msg msg = new Msg();
        try{
            JSONObject jsonObj = JSONObject.parseObject(data);
            JSONObject jsonObject = new JSONObject();
            User newUser = new User();
            String userToken = jsonObj.getString("userToken");
            newUser.setToken(userToken);
            User userData = userService.loginByToken(userToken);
            if(userData.getIsok()<1){
                SecurityUtils.getSubject().logout();
                msg.setInfo("账号暂时无法使用，请咨询管理员");
                msg.setCode("110403");
                return msg;
            }
            String jwtToken = JWTUtil.createToken(userData);
            UploadConfig uploadConfig = uploadConfigService.getUpdateConfig();
            jsonObject.put("username", userData.getUsername());
            jsonObject.put("jwttoken", jwtToken);
            jsonObject.put("suffix",uploadConfig.getSuffix().split(","));
            jsonObject.put("myImgTotal", imgService.countimg(userData.getId()));
            jsonObject.put("filesize",Integer.valueOf(uploadConfig.getFilesizeuser())/1024);
            jsonObject.put("imgcount",uploadConfig.getImgcountuser());
            jsonObject.put("uploadSwitch",uploadConfig.getUserclose());
            long memory = Long.valueOf(userData.getMemory());
            Long usermemory = imgService.getusermemory(userData.getId())==null?0L:imgService.getusermemory(userData.getId());
            if(memory==0){
                jsonObject.put("myMemory","无容量");
            }else{
                Double aDouble = Double.valueOf(String.format("%.2f", (((double)usermemory/(double)memory)*100)));
                if(aDouble>=999){
                    jsonObject.put("myMemory",999);
                }else{
                    jsonObject.put("myMemory",aDouble);
                }
            }
            msg.setData(jsonObject);
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("110500");
            msg.setInfo("未获取到用户，请先在设置中添加Token");
        }
        return msg;
    }

    @PostMapping("/imgUpload")
    public Msg imgUpload(HttpServletRequest request, @RequestParam(required = true, value = "file") MultipartFile multipartFile,
                         @RequestParam(required = true, value = "days") String days)  {
        Msg msg = new Msg();
        try{
            if (null != multipartFile) {
                String originalFilename = multipartFile.getOriginalFilename();
                if(StringUtils.isBlank(originalFilename)){
                    originalFilename = "未命名图像";
                }
                File file = SetFiles.changeFile_new(multipartFile);
                msg = uploadServicel.uploadForLoc(request,file,originalFilename,Integer.valueOf(days),null,null);
            }else{
                msg.setCode("500");
            }
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("500");
        }
        return msg;
    }

    @PostMapping("/imgUploadForCopy")
    public Msg imgUploadForCopy(@RequestParam(required = true, value = "data") String data, HttpServletRequest request)  {
        Msg msg = new Msg();
        try{
            JSONObject jsonObj = JSONObject.parseObject(data);
            String imgstr = jsonObj.getString("imgstr");
            Integer days = jsonObj.getInteger("days");
            String md5 = jsonObj.getString("md5");
            if (null != imgstr && !imgstr.isEmpty()) {
                MultipartFile multipartFile = Base64ToMultipartFile.base64Convert(imgstr);
                File file = SetFiles.changeFile_new(multipartFile);
                msg = uploadServicel.uploadForLoc(request, file,"未命名图像", days, null,md5);
            }
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode("500");
        }
        return msg;
    }



}
