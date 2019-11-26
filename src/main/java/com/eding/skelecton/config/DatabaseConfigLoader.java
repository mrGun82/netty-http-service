package com.eding.skelecton.config;

import com.eding.skelecton.database.JDBCOperator;
import lombok.Getter;

import java.util.Map;
import java.util.Objects;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 09:35
 */
@Getter
public class DatabaseConfigLoader extends AbstractConfigLoader implements ConfigLoader {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String cachePrepStmts;
    private String prepStmtCacheSize;
    private String prepStmtCacheSqlLimit;
    private String connectionTimeout;
    private String idleTimeout;
    private String maxLifetime;
    private String minimumIdle;
    private String maximumPoolSize;
    private String poolName;
    private String leakDetectionThreshold;

    @Override
    public void loadConfig() {
        if (Objects.isNull(getAppConfig())) {
            throw new RuntimeException("配置文件获取失败");
        }
        Map<String, String> dbMap = getAppConfig().getDatabase();
        url = dbMap.get("url");
        username = dbMap.get("username");
        password = dbMap.get("password");
        driverClassName = dbMap.get("driverClassName");
        cachePrepStmts = dbMap.get("cachePrepStmts");
        prepStmtCacheSize = dbMap.get("prepStmtCacheSize");
        prepStmtCacheSqlLimit = dbMap.get("prepStmtCacheSqlLimit");
        connectionTimeout = dbMap.get("connectionTimeout");
        idleTimeout = dbMap.get("idleTimeout");
        maxLifetime = dbMap.get("maxLifetime");
        minimumIdle = dbMap.get("minimumIdle");
        maximumPoolSize = dbMap.get("maximumPoolSize");
        poolName = dbMap.get("poolName");
        leakDetectionThreshold = dbMap.get("leakDetectionThreshold");
        JDBCOperator.getInstance();
    }
}
