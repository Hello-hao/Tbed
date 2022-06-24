package cn.hellohao.utils.test;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.UUID;

class counts {
    public static Long i = 5000L;
}
//1.创建一个实现了Runnable接口的类
class Demo implements Runnable {
    //2.实现类去实现(重写)Runnable中的抽象方法：run()
    @Override
    public void run() {

        while (true){
            try{
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("email", uuid.substring(0,10)+"@163.com");
                paramMap.put("pwd", uuid+uuid+uuid);
                String result1 = HttpUtil.post("http://39.107.115.48/og/v1/user/login/email", paramMap);
                JSONObject jsonObject = JSONObject.parseObject(result1);
                System.out.println("执行第"+counts.i+"次：响应代码："+jsonObject.getInteger("c")+"，"+jsonObject.getString("m"));
                System.out.println("用户UUID："+jsonObject.getJSONObject("d").getJSONObject("user").getString("object_id"));
                if(jsonObject.getInteger("c")==200){
                    String uuid2 = UUID.randomUUID().toString().replace("-", "").toLowerCase();
                    HashMap<String, Object> paramMap2 = new HashMap<>();
                    paramMap2.put("order_id", uuid2);
                    paramMap2.put("email", uuid.substring(0,10)+"@163.com");
                    paramMap2.put("info", "pay&money=永久会员 ¥12 (随功能增多会涨价)");
                    paramMap2.put("product_id", "beetodo");
                    paramMap2.put("pay_channel", "unkown");
                    paramMap2.put("click_pay_time", "unkown");
                    paramMap2.put("version", "21/1.2.6");
                    String result2 = HttpUtil.post("http://39.107.115.48/og/v1/user/login/email", paramMap);
                    JSONObject jsonObject2 = JSONObject.parseObject(result2);
                    System.out.println("提交订单的："+jsonObject2.getInteger("c"));
                }
                counts.i++;
            }catch (Exception e){
                e.printStackTrace();
                System.err.println("报错了");
            }
        }

    }
}

//测试类:
class ThreadTest {
    public static void main(String[] args) {

        //同样为了展示多线程效果,我们在main主线程中也遍历输出一下作为对比
        for (int i = 0; i < 10; i++) {
            //3.创建实现类的对象
            Demo d1 = new Demo();
//4.将此对象作为参数传递到Thread类的构造器中，创建Thread类的对象
            Thread thread = new Thread(d1);
            //5.通过Thread类的对象调用start()
            thread.start();
        }
    }
}