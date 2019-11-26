package com.eding.action;

import com.eding.annotations.EDAction;
import com.eding.annotations.EDComponent;
import com.eding.annotations.EDParameter;
import com.eding.exceptions.Result;
import com.eding.mapper.TestMapper;
import com.eding.model.TestDO;
import com.eding.skelecton.database.JDBCOperator;
import com.eding.skelecton.database.SQLUtils;
import com.eding.skelecton.database.SqlSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-22 15:59
 * {
 * "action":"example.test2",
 * "parameter":{
 * "one":"1",
 * "two":"2",
 * "three":"3"
 * }
 * }
 */
@EDComponent("example")
public class ExampleAction {
    @EDAction(action = "test1")
    public Result fun() {
        System.out.println("执行无参方法");
        return Result.ok("执行无参方法");
    }

    @EDAction(action = "test2")
    public Result fun(@EDParameter(name = "one") int one,
                      @EDParameter(name = "two") String two,
                      @EDParameter(name = "three") boolean three) throws Exception {
        System.out.println("执行多参方法： one = " + one + " two = " + two + " three = " + three);

        TestMapper mapper = SqlSession.getMapper(TestMapper.class);
        TestDO test = mapper.selectUser("1");
        System.out.println(test);
        Statement statement = JDBCOperator.getInstance().statement();
        ResultSet rs = statement.executeQuery("select * from test ");
        while (rs.next()) {
            System.out.println("rs.getString(1): " + rs.getString(1));
        }

        return Result.ok("执行多参方法： one = " + one + " two = " + two + " three = " + three);
    }

}
