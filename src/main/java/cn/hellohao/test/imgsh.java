package cn.hellohao.test;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import org.json.JSONObject;

public class imgsh {
    //设置APPID/AK/SK
    private static final String APP_ID = "15434135";
    private static final String API_KEY = "yDdHaeMf0FfFAxupXFkoxoPG";
    private static final String SECRET_KEY = "QGRXTy8QNDMr0dWuWS6bj7rNcwtEddKe";

    private imgsh() {
    }

    ;

    private static AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);

    public static AipContentCensor getConn() {
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }

    private static AipContentCensor chushihua() {
        // 初始化一个AipImageCensor
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        //AipImageCensor client = new AipImageCensor(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
        //client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量
        //System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

//        // 调用接口
//        String path = "test.jpg";
//        JSONObject res = client.antiPorn(path);
//        System.out.println(res.toString(2));
        return client;
    }

    public static void main(String[] args) {
        AipContentCensor client = chushihua();
        sample(client);


    }

    public static void sample(AipContentCensor client) {
//        // 参数为本地图片路径
//        String path = "test.jpg";
        //       JSONObject response = client.imageCensorUserDefined(path, EImgType.FILE, null);
//        System.out.println(response.toString());

        // 参数为url
        //String url = "https://hellohao.nos-eastchina1.126.net/BlogImg/%E8%BF%99%E6%98%AF%E6%B5%8B%E8%AF%95%E6%96%87%E4%BB%B6%E5%A4%B9/huang.png";
        String url = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4225605092,1601815977&fm=26&gp=0.jpg";
        JSONObject res = client.antiPorn(url);
        res = client.imageCensorUserDefined(url, EImgType.URL, null);
        System.out.println(res.toString());

//        // 参数为本地图片文件二进制数组
//        byte[] file = readImageFile(imagePath);
//        response = client.imageCensorUserDefined(file, null);
//        System.out.println(response.toString());
    }
}
