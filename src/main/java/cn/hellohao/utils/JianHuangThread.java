package cn.hellohao.utils;

import cn.hellohao.pojo.Imgreview;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.User;
import cn.hellohao.service.ImgreviewService;
import java.util.Map;

public class JianHuangThread extends Thread {

    private Keys key;
    private Map hashMap;
    private ImgreviewService imgreviewService;
    private User user;

    public JianHuangThread(ImgreviewService imgreviewService, Keys keys, User user, Map<String, Integer> hashMap) {
        this.key = keys;
        this.user = user;
        this.imgreviewService = imgreviewService;
        this.hashMap = hashMap;
    }

    @Override
    public void run() {
        Imgreview imgreview = imgreviewService.selectByPrimaryKey(1);
        if (user != null) {
            ImageReview.imgJB(hashMap, key.getRequestAddress() + "/", key, imgreview);
        } else {
            ImageReview.imgJB(hashMap, key.getRequestAddress() + "/", key, imgreview);
        }
    }
}
