package cn.hellohao.utils;

import cn.hellohao.pojo.Keys;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class StringUtils {
    public static Boolean doNull(Integer sourcekey,Keys k) {
        if(sourcekey==1){
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                    && k.getBucketname()!=null && k.getRequestAddress()!=null ){
                if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                        && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ){
                    return true;
                }
            }
        }else if(sourcekey==2){
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                    && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
                if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                        && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ) {
                    return true;
                }
            }
        }else if(sourcekey==3){
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null
                    && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
                if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("")
                        && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ) {
                    return true;
                }
            }
        }else if(sourcekey==4){
            if (k.getEndpoint() != null && k.getAccessSecret() != null && k.getEndpoint() != null
                    && k.getBucketname() != null && k.getRequestAddress() != null) {
                if (!k.getEndpoint().equals("") && !k.getAccessSecret() .equals("") && !k.getEndpoint() .equals("")
                        && !k.getBucketname() .equals("") && !k.getRequestAddress().equals("")) {
                    return true;
                }
            }
        }else if(sourcekey==6){
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null
                    && k.getBucketname()!=null && k.getRequestAddress()!=null ) {
                if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("")
                        && !k.getBucketname().equals("") && !k.getRequestAddress().equals("") ) {
                    return true;
                }
            }
        }else if(sourcekey==7){
            if(k.getEndpoint()!=null && k.getAccessSecret()!=null && k.getEndpoint()!=null && k.getRequestAddress()!=null ) {
                if(!k.getEndpoint().equals("") && !k.getAccessSecret().equals("") && !k.getEndpoint().equals("") && !k.getRequestAddress().equals("") ) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 移除 URL 中的第一个 '/'
     * @return 如 path = '/folder1/file1', 返回 'folder1/file1'
     */
    public static String removeFirstSeparator(String path) {
        if (!"".equals(path) && path.charAt(0) == '/') {
            path = path.substring(1);
        }
        return path;
    }
    /**
     * 移除 URL 中的最后一个 '/'
     * @return 如 path = '/folder1/file1/', 返回 '/folder1/file1'
     */
    public static String removeLastSeparator(String path) {
        if (!"".equals(path) && path.charAt(path.length() - 1) == '/') {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    /**
     * 将域名和路径组装成 URL, 主要用来处理分隔符 '/'
     * @param domain    域名
     * @param path      路径
     * @return          URL
     */
    public static String concatDomainAndPath(String domain, String path) {
        if (path != null && path.length() > 1 && path.charAt(0) != '/') {
            path = '/' + path;
        }
        if (domain.charAt(domain.length() - 1) == '/') {
            domain = domain.substring(0, domain.length() - 2);
        }
        return domain + path;
    }
}
