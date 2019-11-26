package com.eding.skelecton.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 14:32
 */
public class ParameterMaker {
    // 注解Parameter中name的值 +  参数对象转换成的gson字符串所形成的map
    private Map<String, String> parameterMap;
    private Gson gson;

    public ParameterMaker() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
        parameterMap = new HashMap<String, String>();
    }

    // 其name就是注解Parameter中name的值，value就是参数的具体值
    public ParameterMaker add(String name, Object value) {
        // 通过gson将参数对象转换为gson字符串
        parameterMap.put(name, gson.toJson(value));
        return this;
    }

    // 将得到的name + 参数对象转换成的gson字符串map再次转换成gson字符串，以便于进行传输
    public String toOgnl() {
        if (parameterMap.isEmpty()) {
            return null;
        }

        return gson.toJson(parameterMap);
    }
}
