package cn.hellohao.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2019/9/20 16:27
 */
public class DateUtils {
     public static String plusDay(int num){
         Date d = new Date();
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String currdate = format.format(d);
         Calendar ca = Calendar.getInstance();
         ca.add(Calendar.DATE, num);
         d = ca.getTime();
         String enddate = format.format(d);
         return enddate;
     }
}
