package com.eding.example.action;

import com.eding.framework.annotations.EDAction;
import com.eding.framework.annotations.EDComponent;
import com.eding.framework.annotations.EDParameter;
import com.eding.framework.exceptions.Result;
import com.eding.example.mapper.TestMapper;
import com.eding.example.model.TestDO;
import com.eding.framework.database.JDBCOperator;
import com.eding.framework.database.SqlSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

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
    @EDAction(action = "selectOne")
    public Result selectOne(@EDParameter(name = "id") String id) {
        TestMapper mapper = SqlSession.getMapper(TestMapper.class);
        TestDO test = mapper.selectTest(id);
        return Result.ok(test);
    }

    @EDAction(action = "selectList")
    public Result selectList(@EDParameter(name = "name") String name) {
        TestMapper mapper = SqlSession.getMapper(TestMapper.class);
        List<TestDO> tests = mapper.selectTests(name);
        return Result.ok(tests);
    }

    @EDAction(action = "insert")
    public Result insert(
            @EDParameter(name = "age") Integer age,
            @EDParameter(name = "name") String name) throws Exception {
        TestMapper mapper = SqlSession.getMapper(TestMapper.class);
        int ic = mapper.insertTest(UUID.randomUUID().toString().replaceAll("-", ""), name, age);
        return Result.ok("insert count ：" + ic);
    }

    @EDAction(action = "update")
    public Result update(
            @EDParameter(name = "age") String id,
            @EDParameter(name = "name") String name,
            @EDParameter(name = "age") Integer age) throws Exception {
        TestMapper mapper = SqlSession.getMapper(TestMapper.class);
        Integer ic = mapper.updateTests(id, name, age);
        return Result.ok("update count ：" + ic);
    }

    @EDAction(action = "jdbc")
    public Result jdbc() throws SQLException {
        Statement statement = JDBCOperator.getInstance().statement();
        ResultSet rs = statement.executeQuery("select * from test ");
        StringBuilder resultStr = new StringBuilder();
        while (rs.next()) {
            resultStr.append("rs.getString(1): " + rs);
        }
        return Result.ok(resultStr.toString());
    }
}
