package cn.hellohao.test;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class jsontest {
    public static void main(String[] args) {
        String str = "[{\"conclusion\":\"不合规\",\"log_id\":15574879277703875,\"data\":[{\"msg\":\"存在色情内容\",\"probability\":1,\"type\":1}],\"conclusionType\":2}]\n";

        JSONArray jsonArray = JSON.parseArray(str);
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            JSONArray data = jsonObject.getJSONArray("data");
            for (Object datum : data) {
                JSONObject aaa = (JSONObject) datum;
                System.out.println(aaa.getString("msg"));
            }
        }
    }
}
