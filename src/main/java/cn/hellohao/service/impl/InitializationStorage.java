package cn.hellohao.service.impl;

import cn.hellohao.auth.filter.SubjectFilter;
import cn.hellohao.dao.KeysMapper;
import cn.hellohao.pojo.Keys;
import cn.hellohao.utils.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/8/28 15:33
 */

@Component
@Order(2)
public class InitializationStorage implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(InitializationStorage.class);
    @Value("${CROS_ALLOWED_ORIGINS}")
    private String allowedOrigins;

    @Autowired
    private KeysMapper keysMapper;

    @Override
    public void run(String... args) {
        SubjectFilter.WEBHOST = allowedOrigins;
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        intiStorage();
        sout();
    }
    public void intiStorage(){
        List<Keys> keylist = keysMapper.getKeys();
        for (Keys key : keylist) {
            if(key.getStorageType()!=0 && key.getStorageType()!=null){
                if(key.getStorageType()==1){
                    NOSImageupload.Initialize(key);
                }else if (key.getStorageType()==2){
                    OSSImageupload.Initialize(key);
                }else if(key.getStorageType()==3){
                    USSImageupload.Initialize(key);
                }else if(key.getStorageType()==4){
                    KODOImageupload.Initialize(key);
                }else if(key.getStorageType()==6){
                    COSImageupload.Initialize(key);
                }else if(key.getStorageType()==7){
                    FtpServiceImpl.Initialize(key);
                }else if(key.getStorageType()==8){
                    S3Imageupload.Initialize(key);
                }else if(key.getStorageType()==9){
                    WebDAVImageupload.Initialize(key);
                }
            }
        }
    }

    public void sout(){
        Print.Normal("______________________________________________");
        Print.Normal("              Hellohao Tbed                ");
        Print.Normal("     Successful startup of the program      ");
        Print.Normal("     is OK!  Open http:// yourIP:port       ");
        Print.Normal("______________________________________________");
    }
}
