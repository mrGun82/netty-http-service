package com.eding.skelecton.action;


import com.eding.annotations.EDComponent;
import com.eding.annotations.EDAction;
import com.eding.annotations.EDParameter;
import com.eding.exceptions.ActionHasDefineException;
import com.eding.exceptions.MethodParameterNotDefineException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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

    public void scanAction(String packageName) throws Exception {
        log.info("Starting scan");
        new ComponentScanner() {
            @Override
            public void dealClass(Class<?> clazz) throws Exception {
                if (!clazz.isAnnotationPresent(EDComponent.class)) {
                    return;
                }
                Object object = clazz.newInstance();
                log.info("scan component: " + clazz.getAnnotation(EDComponent.class).value());
                scanMethod(clazz, object);
            }
        }.scanPackage(packageName);
        log.info("finish scan");
    }

    public void scanAction(Object object) throws Exception {
        scanMethod(object.getClass(), object);
    }

    private void scanMethod(Class<?> clazz, Object object) throws Exception {

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            if (!method.isAnnotationPresent(EDAction.class)) {
                continue;
            }
            EDAction edAction = method.getAnnotation(EDAction.class);
            String action = edAction.action();

            if (actionDefinations.get(action) != null) {
                throw new ActionHasDefineException("方法" + action + "已定义！");
            }
            log.info("scan action: " + action);
            Parameter[] parameters = method.getParameters();
            List<Parameter> parameterList = new ArrayList<Parameter>();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                if (!parameters[i].isAnnotationPresent(EDParameter.class)) {
                    throw new MethodParameterNotDefineException("第" + (i + 1) + "个参数未定义！");
                }
                parameterList.add(parameter);
                log.info(action + " action add parameter: " + parameter.getAnnotation(EDParameter.class).name());
            }
            addActionDefination(action, clazz, object, method, parameterList);
        }

    }

    private void addActionDefination(String action, Class<?> clazz, Object object, Method method, List<Parameter> parameterList) {
        ActionDefination actionDefination = new ActionDefination(clazz, object, method, parameterList);
        if (Objects.nonNull(actionDefinations.get(action))) {
            throw new RuntimeException("action :" + action + " 已存在!");
        }
        EDComponent ed = clazz.getAnnotation(EDComponent.class);
        actionDefinations.put(ed.value() + "." + action, actionDefination);
        log.info("put action [" + ed.value() + "." + action + "] to actionDefinations");
    }

    protected ActionDefination getActionDefination(String action) {
        return actionDefinations.get(action);
    }
}
