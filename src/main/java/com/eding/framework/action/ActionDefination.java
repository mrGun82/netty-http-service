package com.eding.framework.action;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 13:58
 */
public class ActionDefination<T> {
    private Class<T> clazz;
    private Object object;
    private Method method;
    private List<Parameter> paramerterList;

    protected ActionDefination(Class<T> clazz, Object object, Method method, List<Parameter> paramerterList) {
        this.clazz = clazz;
        this.object = object;
        this.method = method;
        this.paramerterList = paramerterList;
    }

    protected Class<T> getclazz() {
        return clazz;
    }

    protected Object getObject() {
        return object;
    }

    protected Method getMethod() {
        return method;
    }

    protected List<Parameter> getParamerterList() {
        return paramerterList;
    }
}
