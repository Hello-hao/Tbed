package cn.hellohao.quartz;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
 
 
@EnableScheduling
@Component
public class FirstJob{
 
  public void task(){
    //System.out.println("任务1执行....");
    TempImgTask tempImgTask = new TempImgTask();
    try {
      tempImgTask.start();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}