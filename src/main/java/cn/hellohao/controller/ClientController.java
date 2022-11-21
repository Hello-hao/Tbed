package cn.hellohao.controller;

import cn.hellohao.pojo.Msg;
import cn.hellohao.service.impl.ClientService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-07-18 17:22
 */
@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired private ClientService clientService;

    @PostMapping(value = "/uploadbymail")
    @ResponseBody
    public Msg uploadbymail(
            HttpServletRequest request,
            @RequestParam(value = "file") MultipartFile file,
            @RequestParam(value = "mail", defaultValue = "") String mail,
            @RequestParam(value = "pass", defaultValue = "") String pass,
            @RequestParam(value = "days", defaultValue = "0") String days) {
        if (file == null
                || StringUtils.isBlank(mail)
                || StringUtils.isBlank(pass)) {
            Msg msg = new Msg();
            msg.setCode("400");
            msg.setInfo("相关参数不能为空");
            return msg;
        }
        Msg resultBean =
                clientService.uploadImg(
                        request, file, mail, pass, Integer.valueOf(days.toString()));
        return resultBean;
    }
}
