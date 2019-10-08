package cn.hellohao.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tiansh
 * @version 1.0
 * @date 2019/9/20 16:27
 */
public class DateUtils {
 /**
      * 当前日期加上天数后的日期
      * @param num 为增加的天数
      * @return
      */
     public static String plusDay(int num){
         Date d = new Date();
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String currdate = format.format(d);
         System.out.println("现在的日期是：" + currdate);

         Calendar ca = Calendar.getInstance();
         ca.add(Calendar.DATE, num);// num为增加的天数，可以改变的
         d = ca.getTime();
         String enddate = format.format(d);
         System.out.println("增加天数以后的日期：" + enddate);
         return enddate;
     }
}
