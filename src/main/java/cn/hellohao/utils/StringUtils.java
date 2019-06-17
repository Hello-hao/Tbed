package cn.hellohao.utils;

import cn.hellohao.pojo.Keys;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class StringUtils {
    public static Boolean doNull(Object objs){
        Field[] fields = objs.getClass().getDeclaredFields();
        //用于判断所有属性是否为空,如果参数为空则不查询
        boolean flag = true;
        for (Field field : fields) {
            //不检查 直接取值
            field.setAccessible(true);

            try {
                if(StringUtils.isNull(field.get(objs))) {
                    //不为空
                    flag = false;
                    //当有任何一个参数不为空的时候则跳出判断直接查询
                    //break;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }
    public final static boolean isNull(Object[] objs) {
        if (objs == null || objs.length == 0)
            return true;


        return false;

    }


    public final static boolean isNull(Object obj) {
        if (obj == null || isNull(obj.toString())) {
            return true;
        }


        return false;
    }


    public final static boolean isNull(Integer integer) {
        if (integer == null || integer == 0)
            return true;


        return false;

    }


    public final static boolean isNull(Collection collection) {
        if (collection == null || collection.size() == 0)
            return true;


        return false;
    }


    public final static boolean isNull(Map map) {
        if (map == null || map.size() == 0)
            return true;


        return false;
    }


    public final static boolean isNull(String str) {
        return str == null || "".equals(str.trim()) || "null".equals(str.toLowerCase());
    }


    public final static boolean isNull(Long longs) {
        if (longs == null || longs == 0)
            return true;


        return false;
    }


    public final static boolean isNotNull(Long longs) {
        return !isNull(longs);

    }


    public final static boolean isNotNull(String str) {
        return !isNull(str);
    }


    public final static boolean isNotNull(Collection collection) {
        return !isNull(collection);
    }


    public final static boolean isNotNull(Map map) {
        return !isNull(map);
    }


    public final static boolean isNotNull(Integer integer) {
        return !isNull(integer);

    }


    public final static boolean isNotNull(Object[] objs) {
        return !isNull(objs);

    }


    public final static boolean isNotNull(Object obj) {
        return !isNull(obj);

    }


}
