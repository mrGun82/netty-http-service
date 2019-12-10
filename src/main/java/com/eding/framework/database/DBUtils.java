package com.eding.framework.database;

import com.eding.framework.config.AppConfigLoader;
import com.eding.framework.config.DatabaseConfigLoader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.util.List;
import java.util.Map;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-12-03 17:26
 */
@Slf4j
public class DBUtils {
    private static HikariDataSource dataSource;
    private static QueryRunner queryRunner;

    static {
        DatabaseConfigLoader databaseConfigLoader = AppConfigLoader.getInstance().getDatabaseConfigLoader();
        String driverClassName = databaseConfigLoader.getDriverClassName();
        String url = databaseConfigLoader.getUrl();
        String username = databaseConfigLoader.getUsername();
        String password = databaseConfigLoader.getPassword();
        String cachePrepStmts = databaseConfigLoader.getCachePrepStmts();
        String prepStmtCacheSize = databaseConfigLoader.getPrepStmtCacheSize();
        String prepStmtCacheSqlLimit = databaseConfigLoader.getPrepStmtCacheSqlLimit();
        String connectionTimeout = databaseConfigLoader.getConnectionTimeout();
        String idleTimeout = databaseConfigLoader.getIdleTimeout();
        String maxLifetime = databaseConfigLoader.getMaxLifetime();
        String minimumIdle = databaseConfigLoader.getMinimumIdle();
        String maximumPoolSize = databaseConfigLoader.getMaximumPoolSize();
        String poolName = databaseConfigLoader.getPoolName();
        String leakDetectionThreshold = databaseConfigLoader.getLeakDetectionThreshold();

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.addDataSourceProperty("cachePrepStmts", cachePrepStmts);
        config.addDataSourceProperty("prepStmtCacheSize", prepStmtCacheSize);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", prepStmtCacheSqlLimit);
        config.addDataSourceProperty("connectionTimeout", connectionTimeout);
        config.addDataSourceProperty("idleTimeout", idleTimeout);
        config.addDataSourceProperty("maxLifetime", maxLifetime);
        config.addDataSourceProperty("minimumIdle", minimumIdle);
        config.addDataSourceProperty("maximumPoolSize", maximumPoolSize);
        config.addDataSourceProperty("poolName", poolName);
        config.addDataSourceProperty("leakDetectionThreshold", leakDetectionThreshold);
        dataSource = new HikariDataSource(config);
        queryRunner = new QueryRunner(dataSource);
    }

    public static <T> T query(String sql, ResultSetHandler<T> resultSetHandler, Object... params) {
        T result = null;
        try {
            result = queryRunner.query(sql, resultSetHandler, params);
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    public static int update(String sql, Object... params) {
        int result = 0;
        try {
            result = queryRunner.update(sql, params);
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    public static int insert(String sql, Object... params) {
        int result = 0;
        try {
            result = queryRunner.execute(sql, params);
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    public static Map<String, Object> findById(String table, int id) {
        String sql = "select * from " + table + " where id = ?";
        return query(sql, new MapHandler(), id);
    }

    public static <T> T findById(String table, int id, BeanHandler<T> beanHandler) {
        String sql = "select * from " + table + " where id = ?";
        return query(sql, beanHandler, id);
    }

    public static List<Map<String, Object>> findByCondition(String table, String condition) {
        String sql = "select * from " + table + " where " + condition;
        return query(sql, new MapListHandler());
    }

    public static <T> List<T> findByCondition(String table, String condition, BeanListHandler<T> beanListHandler) {
        String sql = "select * from " + table + " where " + condition;
        return query(sql, beanListHandler);
    }

    public static List<Map<String, Object>> findByCondition(String table, String condition, String sort) {
        String sql = "select * from " + table + " where " + condition + "order by " + sort;
        return query(sql, new MapListHandler());
    }

    public static List<Map<String, Object>> findByCondition(String table, String condition, String sort, String limit) {
        String sql = "select * from " + table + " where " + condition + "order by " + sort + limit;
        return query(sql, new MapListHandler());
    }

    public static void close() {
        dataSource.close();
    }

}
