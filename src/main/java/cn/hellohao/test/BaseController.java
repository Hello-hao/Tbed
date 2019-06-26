package cn.hellohao.test;


import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

/**
 * @author 
 * 
 */  
 
public class BaseController {

    public static void main(String[] args) throws  Exception {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        System.out.println( mem.getUsed());
    }
 
}