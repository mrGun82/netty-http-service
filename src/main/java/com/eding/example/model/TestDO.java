package com.eding.example.model;

import lombok.Data;
import lombok.ToString;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 14:30
 */
@Data
@ToString
public class TestDO {
    private String id;
    private String name;
    private Integer age;
}
