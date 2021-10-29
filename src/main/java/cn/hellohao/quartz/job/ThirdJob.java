package cn.hellohao.quartz.job;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
//@EnableScheduling
public class ThirdJob {

	public void task() {
		System.out.println("任务3执行....当前时间为" + dateFormat().format(new Date()));
	}

	private SimpleDateFormat dateFormat() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		return simpleDateFormat;
	}
}
