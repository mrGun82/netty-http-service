package com.eding.skelecton.database;

import com.eding.skelecton.database.annotations.ExtInsert;
import com.eding.skelecton.database.annotations.ExtParam;
import com.eding.skelecton.database.annotations.ExtSelect;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 13:46
 */
public class DatabaseInvocationHandler implements InvocationHandler {

    private Object subject;

    public DatabaseInvocationHandler(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ExtInsert extInsert = method.getAnnotation(ExtInsert.class);
        if (extInsert != null) {
            return insertSQL(extInsert, method, args);
        }
        ExtSelect extSelect = method.getAnnotation(ExtSelect.class);
        if (extSelect != null) {
            return select(extSelect, method, args);
        }
        return null;
    }


    public int insertSQL(ExtInsert extInsert, Method method, Object[] args) throws SQLException {
        String insertSql = extInsert.value();
        System.out.println("sql:" + insertSql);
        Parameter[] parameters = method.getParameters();
        ConcurrentHashMap<Object, Object> parameterMap = getExtParams(parameters, args);
        String[] sqlParameter = SQLUtils.sqlInsertParameter(insertSql);
        List<Object> parameValues = Lists.newArrayList();
        for (int i = 0; i < sqlParameter.length; i++) {
            String str = sqlParameter[i];
            Object object = parameterMap.get(str);
            parameValues.add(object);
        }
        String newSql = SQLUtils.parameQuestion(insertSql, sqlParameter);
        System.out.println("newSql:" + newSql);
        int insertResult = JDBCOperator.getInstance().insert(newSql, false, parameValues);
        return insertResult;
    }

    public Object select(ExtSelect extInsert, Method method, Object[] args) throws SQLException {
        try {
            String selectSQL = extInsert.value();
            Parameter[] parameters = method.getParameters();
            ConcurrentHashMap<Object, Object> parameterMap = getExtParams(parameters, args);
            List<String> sqlSelectParameter = SQLUtils.sqlSelectParameter(selectSQL);
            List<Object> parameValues = Lists.newArrayList();
            for (int i = 0; i < sqlSelectParameter.size(); i++) {
                String parameterName = sqlSelectParameter.get(i);
                Object object = parameterMap.get(parameterName);
                parameValues.add(object.toString());
            }
            String newSql = SQLUtils.parameQuestion(selectSQL, sqlSelectParameter);
            System.out.println("执行SQL:" + newSql + "参数信息:" + parameValues.toString());
            ResultSet rs = JDBCOperator.getInstance().query(newSql, parameValues);
            Class<?> returnType = method.getReturnType();
            if (!rs.next()) {
                return null;
            }
            // 还原游标
            rs.previous();
            // 实例化对象
            Object newInstance = returnType.newInstance();
            List<String> sqlSelectColumn = SQLUtils.sqlSelectColumn(newSql);
            while (rs.next()) {
                for (String parameterName : sqlSelectColumn) {
                    Object value = rs.getObject(parameterName);
                    Field field = returnType.getDeclaredField(parameterName);
                    field.setAccessible(true);
                    field.set(newInstance, value);
                }
            }
            return newInstance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ConcurrentHashMap<Object, Object> getExtParams(Parameter[] parameters, Object[] args) {
        // 获取方法上参数集合
        ConcurrentHashMap<Object, Object> parameterMap = new ConcurrentHashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            ExtParam extParam = parameter.getDeclaredAnnotation(ExtParam.class);
            String paramValue = extParam.value();
            Object oj = args[i];
            parameterMap.put(paramValue, oj);
        }
        return parameterMap;
    }
}
