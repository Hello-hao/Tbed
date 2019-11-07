package cn.hellohao.utils;

import cn.hellohao.service.impl.ImgServiceImpl;
import cn.hellohao.service.impl.ImgreviewServiceImpl;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/11/6 17:35
 */
public class GetIPS implements Runnable {
    private String imgname ;


    public void setImgname(String imgname) {
        this.imgname = imgname;
    }

    @Override
    public void run() {
        ImgServiceImpl imgService = SpringContextHolder.getBean(ImgServiceImpl.class);
        imgService.setabnormal(imgname,Sentence.getIPs());
    }

    public static void runxc(String imgnames){
        GetIPS getIPS = new GetIPS();
        getIPS.setImgname(imgnames);
        Thread thread = new Thread(getIPS);
        thread.start();
    }
}
