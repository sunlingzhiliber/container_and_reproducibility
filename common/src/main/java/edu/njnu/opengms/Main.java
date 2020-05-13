package edu.njnu.opengms;

import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Main
 * @Description todo
 * @Author sun_liber
 * @Date 2019/11/22
 * @Version 1.0.0
 */
public class Main {
    public static void main(String[] args) {

       Map<String,String> map=new HashMap<>();
       map.put("1","1");
       map.put("2","2");

        System.out.println(JSONUtil.toJsonStr(map));

//        Stream.of("1","2","3").forEach(stringConsumer);
    }
}
