package com.eding.mapper;

import com.eding.model.TestDO;
import com.eding.skelecton.database.annotations.ExtInsert;
import com.eding.skelecton.database.annotations.ExtParam;
import com.eding.skelecton.database.annotations.ExtSelect;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 14:27
 */
public interface TestMapper {
    @ExtInsert("insert into test(id,name,age) values(#{id},#{name},#{age})")
    public int insertUser(@ExtParam("id") String id, @ExtParam("name") String userName, @ExtParam("age") Integer userAge);

    @ExtSelect("select id,name,age from test where id=#{id}")
    TestDO selectUser(@ExtParam("id") String id);
}
