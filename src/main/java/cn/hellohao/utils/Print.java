package cn.hellohao.utils;

public class Print {
//    正常normal
//    警告 warning

    /*  样式：

    0  空样式

    1  粗体

    4  下划线

    7  反色

    颜色1：

    30  白色

    31  红色

    32  绿色

    33  黄色

    34  蓝色

    35  紫色

    36  浅蓝

    37  灰色

    背景颜色：

    40-47 和颜色顺序相同

    颜色2：

    90-97  比颜色1更鲜艳一些，我也不太清楚为什么又两种
    * */
    public static void Normal(Object object){
        System.out.println("\033[32;1m" + object + "\033[0m");
    }
    public static void warning(Object object){
        System.out.println("\033[31;1m" + object + "\033[0m");
    }

}
