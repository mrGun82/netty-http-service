package com.eding.http.request;

import lombok.Data;

import java.util.Map;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-22 15:54
 */
@Data
public class Request {
    private String action;
    private Map<String,Object> parameter;
}
