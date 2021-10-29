package cn.hellohao.controller;

import cn.hellohao.pojo.Msg;
import cn.hellohao.service.impl.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019-07-18 17:22
 */
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;


    @PostMapping(value = "/uploadbymail")
    @ResponseBody
    public Msg uploadbymail(HttpServletRequest request, @RequestParam("file") MultipartFile file, String mail, String pass)  {
        Msg resultBean = clientService.uploadImg(request, file, mail, pass);
        return resultBean;
    }


}
