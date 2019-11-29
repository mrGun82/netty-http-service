package com.eding.framework.config;

import lombok.Data;

import java.util.Map;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-25 17:45
 */
@Data
public class AppConfig {
    private Map<String, String> server;
    private Map<String, String> packages;
    private Map<String, String> database;
}
