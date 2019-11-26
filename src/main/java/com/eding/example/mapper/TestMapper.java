package com.eding.example.mapper;

import com.eding.example.model.TestDO;
import com.eding.skelecton.database.annotations.ExtInsert;
import com.eding.skelecton.database.annotations.ExtParam;
import com.eding.skelecton.database.annotations.ExtSelect;
import com.eding.skelecton.database.annotations.ExtSelectOne;

import java.util.List;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 14:27
 */
public interface TestMapper {
    @ExtInsert("insert into test(id,name,age) values(#{id},#{name},#{age})")
    int insertTest(@ExtParam("id") String id, @ExtParam("name") String name, @ExtParam("age") Integer age);

    @ExtSelectOne("select id,name,age from test where id=#{id}")
    TestDO selectTest(@ExtParam("id") String id);

    @ExtSelect("select id,name,age from test where name=#{name}")
    List<TestDO> selectTests(@ExtParam("name") String name);
}
