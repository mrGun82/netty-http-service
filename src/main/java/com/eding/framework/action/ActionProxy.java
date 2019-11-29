package com.eding.framework.action;

import com.eding.framework.annotations.EDParameter;
import com.eding.framework.exceptions.ActionNotDefineException;
import com.eding.framework.exceptions.MethodParameterNotMatchException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 14:34
 */
public class ActionProxy {
    private static final Gson gson;
    private static final Type type;

    static {
        gson = new GsonBuilder().disableHtmlEscaping().create();
        type = new TypeToken<Map<String, String>>() {
        }.getType();
    }

    public String doRequest(String action, String parameter) throws Exception {
        ActionDefination ad = ActionFactory.newInstance().getActionDefination(action);
        if (ad == null) {
            throw new ActionNotDefineException("方法" + action + "未定义！");
        }
        Object object = ad.getObject();
        Method method = ad.getMethod();
        Object[] parameters = getParameterArr(parameter, ad.getParamerterList());
        Object result = method.invoke(object, parameters);
        return gson.toJson(result);
    }

    private Object[] getParameterArr(String parameterString, List<Parameter> parameterList) {
        Object[] results = new Object[parameterList.size()];
        if (parameterList.size() == 0) {
            return results;
        }
        Map<String, String> parameterStringMap = gson.fromJson(parameterString, type);
        if (Objects.isNull(parameterStringMap) || (parameterStringMap.size() != parameterList.size())) {
            throw new MethodParameterNotMatchException();
        }
        int index = 0;
        for (Parameter parameter : parameterList) {
            String key = parameter.getAnnotation(EDParameter.class).name();
            if (!parameterStringMap.containsKey(key)) {
                throw new ActionNotDefineException("action 未定义");
            }
            Object value = gson.fromJson(parameterStringMap.get(key), parameter.getParameterizedType());
            results[index++] = value;
        }
        return results;
    }
}