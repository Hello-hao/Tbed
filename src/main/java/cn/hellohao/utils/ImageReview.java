package cn.hellohao.utils;

import cn.hellohao.pojo.Imgreview;
import cn.hellohao.pojo.Keys;
import cn.hellohao.service.ImgreviewService;
import cn.hellohao.service.impl.ImgServiceImpl;
import cn.hellohao.service.impl.ImgreviewServiceImpl;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ImageReview {

    private static AipContentCensor client;

    private ImageReview() {
    }
    private static void Initializes(Imgreview imgreview) {
        client = new AipContentCensor(imgreview.getAppId(), imgreview.getApiKey(), imgreview.getSecretKey());
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    public static void main(String[] args) {

    }
    //开始调用鉴黄。
    public static void imgJB(Map<String, Integer> imgmap, String requestAddress, Keys key, Imgreview imgreview) {
        ImgServiceImpl imgService = SpringContextHolder.getBean(ImgServiceImpl.class);
        ImgreviewServiceImpl imgreviewService = SpringContextHolder.getBean(ImgreviewServiceImpl.class);
        ImageReview.Initializes(imgreview);
        //遍历map,获取里边的key（图片地址）
        for (Map.Entry<String, Integer> entry : imgmap.entrySet()) {
            System.out.println("正在鉴定的图片：" + entry.getKey());
            JSONObject res = client.antiPorn(entry.getKey());
            res = client.imageCensorUserDefined(entry.getKey(), EImgType.URL, null);

            com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray("[" + res.toString() + "]");
            for (Object o : jsonArray) {
                com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) o;
                com.alibaba.fastjson.JSONArray data = jsonObject.getJSONArray("data");
                Integer conclusionType = jsonObject.getInteger("conclusionType");
                if (conclusionType == 2) {
                    for (Object datum : data) {
                        com.alibaba.fastjson.JSONObject imgdata = (com.alibaba.fastjson.JSONObject) datum;
                        if (imgdata.getInteger("type") == 1) {
                            //标记图片
                            String imgname = SetText.getSubString(entry.getKey(), requestAddress, "");
                            Integer ret = imgService.deleimgname(imgname); //删除数据库图片信息
                            //Imgreview imgreview1  =imgreviewService.selectByPrimaryKey(1);
                            Imgreview imgreview1 = new Imgreview();
                            imgreview1.setId(1);
                            Integer count = imgreview.getCount();
                            System.out.println("总数：" + count);
                            imgreview1.setCount(count + 1);
                            imgreviewService.updateByPrimaryKeySelective(imgreview1);

                            if(key.getStorageType()==1){
                                imgService.delect(key, imgname);
                            }else if (key.getStorageType()==2){
                                imgService.delectOSS(key, imgname);
                            }else if(key.getStorageType()==3){
                                //初始化腾讯云
                            }else if(key.getStorageType()==4){
                                //初始化七牛云
                            }else{
                                System.err.println("未获取到对象存储参数，上传失败。");
                            }
                            if (ret == 1) {
                                System.out.println("存在色情图片，删除成功。");
                            } else {
                                System.out.println("存在色情图片，删除失败");
                            }
                        }
                    }
                }
            }
        }
    }


}
