package cn.hellohao.service.impl;

import cn.hellohao.pojo.Msg;
import cn.hellohao.utils.Print;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hellohao.dao.KeysMapper;
import cn.hellohao.pojo.Keys;
import cn.hellohao.service.KeysService;

import java.util.List;

@Service
public class KeysServiceImpl implements KeysService {

    @Autowired
    private KeysMapper keysMapper;

    @Autowired
    private NOSImageupload nOSImageupload;
    @Autowired
    private OSSImageupload ossImageupload;
    @Autowired
    private USSImageupload ussImageupload;
    @Autowired
    private KODOImageupload kodoImageupload;
    @Autowired
    private COSImageupload cosImageupload;
    @Autowired
    private FtpServiceImpl ftpService;
    @Autowired
    private UFileImageupload uFileImageupload;


    @Override
    public Keys selectKeys(Integer id) {
        // TODO Auto-generated method stub
        return keysMapper.selectKeys(id);
    }
    @Override
    public List<Keys> getStorageName() {
        return keysMapper.getStorageName();
    }

    @Override
    public List<Keys> getStorage() {
        return keysMapper.getStorage();
    }

    @Override
    public Msg updateKey(Keys key) {
        Msg msg = new Msg();
        Integer ret = -2;
        if(key.getStorageType()==1){
            ret =nOSImageupload.Initialize(key);
        }else if (key.getStorageType()==2){
            ret = ossImageupload.Initialize(key);
        }else if(key.getStorageType()==3 ){
            ret = ussImageupload.Initialize(key);
        }else if(key.getStorageType()==4){
            ret = kodoImageupload.Initialize(key);
        }else if(key.getStorageType()==5){
            ret = 1;
        }else if(key.getStorageType()==6){
            ret = cosImageupload.Initialize(key);
        }else if(key.getStorageType()==7){
            ret = ftpService.Initialize(key);
        }else if(key.getStorageType()==8){
            ret = uFileImageupload.Initialize(key);
        }else{
            Print.Normal("为获取到存储参数，或者使用存储源是本地的。");
        }
        if(ret>0){
            keysMapper.updateKey(key);
            msg.setInfo("保存成功");
        }else{
            if(key.getStorageType()==5){
                keysMapper.updateKey(key);
                msg.setInfo("保存成功");
            }else{
                msg.setCode("4002");
                msg.setInfo("对象存储初始化失败,请检查参数是否正确");
            }
        }

        return msg;


    }

    @Override
    public List<Keys> getKeys() {
        return keysMapper.getKeys();
    }

}
