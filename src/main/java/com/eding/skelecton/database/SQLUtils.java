package com.eding.skelecton.database;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 14:12
 */
public class SQLUtils {
    /**
     * 获取Insert语句后面values 参数信息<br>
     *
     * @param sql
     * @return
     */
    public static String[] sqlInsertParameter(String sql) {
        int startIndex = sql.indexOf("values");
        int endIndex = sql.length();
        String substring = sql.substring(startIndex + 6, endIndex).replace("(", "").replace(")", "").replace("#{", "").replace("}", "");
        String[] split = substring.split(",");
        return split;
    }

    /**
     * 获取select 后面where语句<br>
     *
     * @param sql
     * @return
     */
    public static List<String> sqlSelectParameter(String sql) {
        int startIndex = sql.indexOf("where");
        int endIndex = sql.length();
        String substring = sql.substring(startIndex + 5, endIndex);
        String[] split = substring.split("and");
        List<String> listArr = Lists.newArrayList();
        for (String string : split) {
            String[] sp2 = string.split("=");
            listArr.add(sp2[0].trim());
        }
        return listArr;
    }

    public static List<String> sqlSelectColumn(String sql) {
        int startIndex = sql.indexOf("select");
        int endIndex = sql.indexOf("from");
        String substring = sql.substring(startIndex + 6, endIndex);
        String[] split = substring.split(",");
        List<String> listArr = Lists.newArrayList();
        for (String string : split) {
            listArr.add(string.trim());
        }
        return listArr;
    }

    /**
     * 将SQL语句的参数替换变为?<br>
     *
     * @param sql
     * @param parameterName
     * @return
     */
    public static String parameQuestion(String sql, String[] parameterName) {
        for (int i = 0; i < parameterName.length; i++) {
            String string = parameterName[i];
            sql = sql.replace("#{" + string + "}", "?");
        }
        return sql;
    }

    public static String parameQuestion(String sql, List<String> parameterName) {
        for (int i = 0; i < parameterName.size(); i++) {
            String string = parameterName.get(i);
            sql = sql.replace("#{" + string + "}", "?");
        }
        return sql;
    }
}
