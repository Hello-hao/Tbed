package cn.hellohao.service.impl;

import cn.hellohao.dao.ImgMapper;
import cn.hellohao.dao.ImgreviewMapper;
import cn.hellohao.pojo.Images;
import cn.hellohao.pojo.Imgreview;
import cn.hellohao.pojo.Keys;
import cn.hellohao.pojo.Msg;
import cn.hellohao.utils.FirstRun;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImgViolationJudgeServiceImpl {

    private static Logger logger = LoggerFactory.getLogger(FirstRun.class);
    @Autowired
    ImgreviewServiceImpl imgreviewService;
    @Autowired ImgMapper imgMapper;
    @Autowired deleImages deleimages;
    @Autowired private ImgreviewMapper imgreviewMapper;


    //synchronized
    @Async("taskExecutor")
    public void LegalImageCheck(Images images, Keys keys) {
        Imgreview imgreview = null;
        try {
            imgreview = imgreviewService.selectByusing(1);
        } catch (Exception e) {
            logger.error("获取鉴别程序的时候发生错误");
            e.printStackTrace();
        }
        // 判断哪个鉴别平台
        if (null != imgreview) {
            LegalImageCheckForBaiDu(imgreview, images, keys);
        }
    }

    private void LegalImageCheckForBaiDu(Imgreview imgreview, Images images, Keys keys) {
        //        Imgreview imgreview = imgreviewService.selectByPrimaryKey(1);
        if (imgreview.getUsing() == 1) {
            logger.info("非法图像鉴别进程启动-BaiDu");
            try {
                AipContentCensor client =
                        new AipContentCensor(
                                imgreview.getAppId(),
                                imgreview.getApiKey(),
                                imgreview.getSecretKey());
                client.setConnectionTimeoutInMillis(5000);
                client.setSocketTimeoutInMillis(30000);
//                 res = client.antiPorn(images.getImgurl());
                org.json.JSONObject res = client.imageCensorUserDefined(images.getImgurl(), EImgType.URL, null);
                logger.info("返回的鉴黄json:" + res.toString());
                com.alibaba.fastjson.JSONArray jsonArray =
                        JSON.parseArray("[" + res.toString() + "]");

                for (Object o : jsonArray) {
                    JSONObject jsonObject =
                            (JSONObject) o;
                    com.alibaba.fastjson.JSONArray data = jsonObject.getJSONArray("data");
                    Integer conclusionType = jsonObject.getInteger("conclusionType");
                    if (conclusionType != null) {
                        if (conclusionType == 2) {
                            // 1：合规，2：不合规，3：疑似，4：审核失败
                            for (Object datum : data) {
                                JSONObject imgdata =
                                        (JSONObject) datum;
                                if (imgdata.getInteger("type") == 1) {
                                    logger.info("存在非法图片，进行处理操作");
                                    // type参数
                                    // 0:百度官方违禁图库、1：色情识别、2：暴恐识别、3：恶心图识别、4:广告检测、5：政治敏感识别、6：图像质量检测、7：用户图像黑名单、8：用户图像白名单、10：用户头像审核、11：百度官方违禁词库、12：图文审核、13:自定义文本黑名单、14:自定义文本白名单、15:EasyDL自定义模型、16：敏感旗帜标志识别、21：不良场景识别、24：直播场景审核
                                    // 存在非法图片，进行处理操作
                                    Images img = new Images();
                                    img.setImgname(images.getImgname());
                                    img.setViolation("1[1]"); // 数字是鉴别平台的主键ID，括号是非法的类型，参考上面的注释
                                    imgMapper.setImg(img);
                                    // 计入总数
                                    Imgreview imgv = new Imgreview();
                                    imgv.setId(1);
                                    Integer count = imgreview.getCount();
                                    imgv.setCount(count + 1);
                                    imgreviewMapper.updateByPrimaryKeySelective(imgv);
                                    Images imgObj =
                                            imgMapper.selectImgUrlByImgUID(images.getImguid());
                                    Msg dele = deleimages.dele2(Long.toString(imgObj.getId()));
                                    List<Long> ids = (List<Long>) dele.getData();
                                    if (!ids.contains(imgObj.getId())) {
                                        logger.error("检测到违规图像，但是数据库删除失败");
                                    }

                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                logger.error("图像鉴黄线程执行过程中出现异常");
                e.printStackTrace();
            }
        }
    }



}
