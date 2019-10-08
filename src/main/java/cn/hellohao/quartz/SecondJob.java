package cn.hellohao.quartz;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
 
 
@EnableScheduling
@Component
public class SecondJob{
 
  public void task() {
    //System.out.println("任务2执行....");
    SchedulerTask schedulerTask = new SchedulerTask();
    schedulerTask.start();
  }
}