package com.eding.action;

import com.eding.annotations.ED;
import com.eding.annotations.EDAction;
import com.eding.annotations.EDParameter;
import com.eding.exceptions.Result;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-22 15:59
 */
@ED("example")
public class ExampleAction {
    @EDAction(action = "test1")
    public Result fun() {
        System.out.println("执行无参方法");
        return Result.ok("执行无参方法");
    }

    @EDAction(action = "test2")
    public Result fun(@EDParameter(name = "one") int one,
                      @EDParameter(name = "two") String two,
                      @EDParameter(name = "three") boolean three) {
        System.out.println("执行多参方法： one = " + one + " two = " + two + " three = " + three);
        return Result.ok("执行多参方法： one = " + one + " two = " + two + " three = " + three);
    }
}
