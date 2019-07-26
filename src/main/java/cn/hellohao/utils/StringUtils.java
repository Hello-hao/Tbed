package cn.hellohao.utils;

import cn.hellohao.pojo.Keys;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class StringUtils {
    //接受当前使用的存储源id，在根据id进行非空判断
    public static Boolean doNull(Integer sourcekey,Keys k) {
        if(sourcekey==1){
            //网易
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                    && k.getBucketname()!=null && k.getRequestAddress()!=null ){
                return true;
            }
        }else if(sourcekey==2){
            //阿里
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                    && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
                return true;
            }
        }else if(sourcekey==3){
            //又拍
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null
                    && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
                return true;
            }

        }else if(sourcekey==4){
            //七牛
            if (k.getEndpoint() != null && k.getAccessSecret() != null && k.getEndpoint() != null
                    && k.getBucketname() != null && k.getRequestAddress() != null) {
                return true;
            }

        }else if(sourcekey==6){
            //腾讯
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                    && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
                return true;
            }
        }
        return false;
    }

}
