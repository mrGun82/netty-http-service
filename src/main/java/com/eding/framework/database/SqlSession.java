package com.eding.framework.database;

import java.lang.reflect.Proxy;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 14:15
 */
public class SqlSession {
    public static <T> T getMapper(Class<T> clazz) throws IllegalArgumentException {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new DatabaseInvocationHandler(clazz));
    }
}
