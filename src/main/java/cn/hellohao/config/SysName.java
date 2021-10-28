package cn.hellohao.config;

/**
 * @author Hellohao
 * @version 1.0
 * @date 2020/5/15 9:20
 */
public class SysName {
    public static final String SYSNAME = "root,hellohaocheck,selectdomain,image,hellohaocheck,HellohaoData,TOIMG," +
            "user,users,admin,retrievepass,deleteimg,hellohaotempimg,360,hellohaotempwatermarimg,components,log";

    public static Boolean CheckSysName(String name){
        boolean b = true;
        if(SYSNAME.contains(name)){
            b = false;
        }
        return b ;
    }

}
