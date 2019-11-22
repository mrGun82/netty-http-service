package com.eding.scanner;


import com.eding.annotations.ED;
import com.eding.annotations.EDAction;
import com.eding.annotations.EDParameter;
import com.eding.exceptions.ActionHasDefineException;
import com.eding.exceptions.MethodParameterNotDefineException;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 13:55
 */
public class ActionFactory {
    private static final Map<String, ActionDefination> actionDefinations = Maps.newConcurrentMap();
    private static final ActionFactory actionFactory = new ActionFactory();

    private ActionFactory() {
    }

    public static ActionFactory newInstance() {
        return actionFactory;
    }

    public void scanAction(Class<?> clazz) throws Exception {
        scanAction(clazz.getPackage().getName());
    }

    //  通过包名称，扫描其下所有文件
    public void scanAction(String packageName) throws Exception {
        new ActionScanner() {
            @Override
            public void dealClass(Class<?> clazz) throws Exception {
                // 处理带有ED注解的类
                if (!clazz.isAnnotationPresent(ED.class)) {
                    return;
                }
                try {
                    Object object = clazz.newInstance();
                    scanMethod(clazz, object);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }.scanPackage(packageName);
    }

    // 通过对象，扫描其所有方法
    public void scanAction(Object object) throws Exception {
        try {
            scanMethod(object.getClass(), object);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void scanMethod(Class<?> clazz, Object object) throws Exception {
        // 获取所有方法
        Method[] methods = clazz.getDeclaredMethods();

        // 遍历所有方法，找到带有Action注解的方法，并得到action的值
        for (Method method : methods) {
            if (!method.isAnnotationPresent(EDAction.class)) {
                continue;
            }
            EDAction edAction = method.getAnnotation(EDAction.class);
            String action = edAction.action();

            // action是否已经定义
            if (actionDefinations.get(action) != null) {
                throw new ActionHasDefineException("方法" + action + "已定义！");
            }

            // 得到所有参数，并判断参数是否满足要求
            Parameter[] parameters = method.getParameters();
            List<Parameter> parameterList = new ArrayList<Parameter>();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (!parameters[i].isAnnotationPresent(EDParameter.class)) {
                    throw new MethodParameterNotDefineException("第" + (i + 1) + "个参数未定义！");
                }
                parameterList.add(parameter);
            }
            // 将得到的结果添加到map中
            addActionDefination(action, clazz, object, method, parameterList);
        }
    }

    private void addActionDefination(String action, Class<?> clazz, Object object, Method method, List<Parameter> parameterList) {
        ActionDefination actionDefination = new ActionDefination(clazz, object, method, parameterList);
        if (Objects.nonNull(actionDefinations.get(action))) {
            throw new RuntimeException("action :" + action + " 已存在!");
        }
        ED ed = clazz.getAnnotation(ED.class);
        actionDefinations.put(ed.value() + "." + action, actionDefination);
    }

    protected ActionDefination getActionDefination(String action) {
        return actionDefinations.get(action);
    }
}
