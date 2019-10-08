package cn.hellohao.quartz;

import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Imgreview;
import cn.hellohao.pojo.Keys;
import cn.hellohao.service.impl.ImgServiceImpl;
import cn.hellohao.service.impl.ImgreviewServiceImpl;
import cn.hellohao.service.impl.KeysServiceImpl;
import cn.hellohao.utils.LocUpdateImg;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
单任务执行，并且通过控制器的接口实现时间间隔的动态修改
任务类
* */
//@Configuration

@Component

public class SchedulerTask {

    @Autowired
    private ImgServiceImpl imgService;
    @Autowired
    private ImgreviewServiceImpl imgreviewService;
    @Autowired
    private KeysServiceImpl keysService;

    private static AipContentCensor client;

    private static SchedulerTask schedulerTask;


    @PostConstruct
    public void init() {
        schedulerTask = this; }


    private static void Initializes(Imgreview imgreview) {
        client = new AipContentCensor(imgreview.getAppId(), imgreview.getApiKey(), imgreview.getSecretKey());
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    public void start(){
        //查询鉴黄key
        Imgreview imgreview = schedulerTask.imgreviewService.selectByPrimaryKey(1);
        if(imgreview.getUsing()==1){
            //初始化鉴黄
            SchedulerTask.Initializes(imgreview);
            //计算出昨天的日期
            Date d = new Date();
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            String oldtime = df.format(new Date(d.getTime() - 1 * 24 * 60 * 60 * 1000));
            //服务器获取出这个时间段的值然后遍历
            List<Images> imglist=  schedulerTask.imgService.gettimeimg(oldtime);
            for (Images images : imglist) {
                String imgurl = images.getImgurl();
                System.err.println("正在鉴定的图片：" + imgurl);
                JSONObject res = client.antiPorn(imgurl);
                res = client.imageCensorUserDefined(imgurl, EImgType.URL, null);
                System.err.println("返回的鉴黄json:"+res.toString());
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
                                Integer ret = schedulerTask.imgService.deleimgname(images.getImgname()); //删除数据库图片信息
                                //Imgreview imgreview1  =imgreviewService.selectByPrimaryKey(1);
                                Imgreview imgreview1 = new Imgreview();
                                imgreview1.setId(1);
                                Integer count = imgreview.getCount();
                                System.out.println("违法图片总数：" + count);
                                imgreview1.setCount(count + 1);
                                schedulerTask.imgreviewService.updateByPrimaryKeySelective(imgreview1);
                                Keys key =null;
                                if(images.getSource()==1){
                                    key = keysService.selectKeys(images.getSource());
                                    schedulerTask.imgService.delect(key, images.getImgname());
                                }else if (images.getSource()==2){
                                    key = keysService.selectKeys(images.getSource());
                                    schedulerTask.imgService.delectOSS(key, images.getImgname());
                                }else if(images.getSource()==3){
                                    key = keysService.selectKeys(images.getSource());
                                    schedulerTask.imgService.delectUSS(key, images.getImgname());
                                }else if(images.getSource()==4){
                                    key = keysService.selectKeys(images.getSource());
                                    schedulerTask.imgService.delectKODO(key, images.getImgname());
                                }else if(images.getSource()==5){
                                    LocUpdateImg.deleteLOCImg(images.getImgname());
                                }else if(images.getSource()==6){
                                    key = keysService.selectKeys(images.getSource());
                                    schedulerTask.imgService.delectCOS(key, images.getImgname());
                                }else if(images.getSource()==7){
                                    key = keysService.selectKeys(images.getSource());
                                    schedulerTask.imgService.delectFTP(key, images.getImgname());
                                }else{
                                    System.err.println("未获取到对象存储参数，上传失败。");
                                }
                                if (ret == 1) {
                                    System.err.println("存在色情图片，删除成功。");
                                } else {
                                    System.err.println("存在色情图片，删除失败");
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}