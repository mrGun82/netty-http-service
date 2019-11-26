package com.eding.skelecton.database;

import com.eding.skelecton.config.AppConfigLoader;
import com.eding.skelecton.config.DatabaseConfigLoader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import java.util.List;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 11:21
 */

public class JDBCOperator {

    @Getter
    @Setter
    public static boolean autoCommit = false;

    private static JDBCOperator connectionManager = new JDBCOperator();

    private HikariDataSource dataSource;

    private JDBCOperator() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static JDBCOperator getInstance() {
        return connectionManager;
    }

    public Connection connection() throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setAutoCommit(autoCommit);
        return connection;
    }

    public Statement statement() throws SQLException {
        Statement statement = null;
        try {
            statement = connection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return statement;
    }

    private PreparedStatement preparedStatement(String SQL, boolean autoGeneratedKeys) throws SQLException {
        PreparedStatement ps = null;
        Connection conn = connection();
        try {
            if (autoGeneratedKeys) {
                ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(SQL);
            }
        } catch (SQLException e) {
            System.out.println("创建 PreparedStatement 对象失败: " + e.getMessage());
        }
        return ps;
    }

    public ResultSet query(String SQL, List<Object> params) throws SQLException {
        if (SQL == null || SQL.trim().isEmpty() || !SQL.trim().toLowerCase().startsWith("select")) {
            throw new RuntimeException("SQL语句为空或不是查询语句");
        }
        ResultSet rs = null;
        if (params.size() > 0) {
            PreparedStatement ps = preparedStatement(SQL, false);
            try {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
                rs = ps.executeQuery();
            } catch (SQLException e) {
                System.out.println("执行SQL失败: " + e.getMessage());
            }
        } else {
            Statement st = statement();
            try {
                rs = st.executeQuery(SQL);
            } catch (SQLException e) {
                System.out.println("执行SQL失败: " + e.getMessage());
            }
        }
        return rs;
    }

    private static Object typeof(Object o) {
        Object r = o;
        if (o instanceof java.sql.Timestamp) {
            return r;
        }
        if (o instanceof java.util.Date) {
            java.util.Date d = (java.util.Date) o;
            r = new java.sql.Date(d.getTime());
            return r;
        }
        if (o instanceof Character || o.getClass() == char.class) {
            r = String.valueOf(o);
            return r;
        }
        return r;
    }

    public boolean execute(String SQL, Object... params) throws SQLException {
        if (SQL == null || SQL.trim().isEmpty() || SQL.trim().toLowerCase().startsWith("select")) {
            throw new RuntimeException("SQL语句为空或有错 SQL: " + SQL);
        }
        boolean r = false;
        SQL = SQL.trim();
        SQL = SQL.toLowerCase();
        String prefix = SQL.substring(0, SQL.indexOf(" "));
        String operation = ""; // 操作类型
        // 根据前缀 确定操作
        switch (prefix) {
            case "create":
                operation = "create table";
                break;
            case "alter":
                operation = "update table";
                break;
            case "drop":
                operation = "drop table";
                break;
            case "truncate":
                operation = "truncate table";
                break;
            case "insert":
                operation = "insert :";
                break;
            case "update":
                operation = "update :";
                break;
            case "delete":
                operation = "delete :";
                break;
        }
        if (params.length > 0) {
            PreparedStatement ps = preparedStatement(SQL, false);
            Connection c = null;
            try {
                c = ps.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < params.length; i++) {
                    Object p = params[i];
                    p = typeof(p);
                    ps.setObject(i + 1, p);
                }
                ps.executeUpdate();
                commit(c);
                r = true;
            } catch (SQLException e) {
                System.out.println(operation + " 失败: " + e.getMessage());
                rollback(c);
            }
        } else {
            Statement st = statement();
            Connection c = null;
            try {
                c = st.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                st.executeUpdate(SQL);
                commit(c); // 提交事务
                r = true;
            } catch (SQLException e) {
                System.out.println(operation + " 失败: " + e.getMessage());
                rollback(c); // 回滚事务
            }
        }
        return r;
    }

    public int insert(String SQL, boolean autoGeneratedKeys, List<Object> params) throws SQLException {
        int var = -1;
        if (SQL == null || SQL.trim().isEmpty()) {
            throw new RuntimeException("没有指定SQL语句，请检查是否指定了需要执行的SQL语句");
        }

        if (!SQL.trim().toLowerCase().startsWith("insert")) {
            System.out.println(SQL.toLowerCase());
            throw new RuntimeException("指定的SQL语句不是插入语句，请检查SQL语句");
        }

        SQL = SQL.trim();
        SQL = SQL.toLowerCase();
        if (params.size() > 0) {
            PreparedStatement ps = preparedStatement(SQL, autoGeneratedKeys);
            Connection c = null;
            try {
                c = ps.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < params.size(); i++) {
                    Object p = params.get(i);
                    p = typeof(p);
                    ps.setObject(i + 1, p);
                }
                int count = ps.executeUpdate();
                if (autoGeneratedKeys) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        var = rs.getInt(1);
                    }
                } else {
                    var = count;
                }
                commit(c);
            } catch (SQLException e) {
                System.out.println("数据保存失败: " + e.getMessage());
                rollback(c);
            }
        } else {
            Statement st = statement();
            Connection c = null;
            try {
                c = st.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                int count = st.executeUpdate(SQL);
                if (autoGeneratedKeys) {
                    ResultSet rs = st.getGeneratedKeys();
                    if (rs.next()) {
                        var = rs.getInt(1);
                    }
                } else {
                    var = count;
                }
                commit(c);
            } catch (SQLException e) {
                System.out.println("数据保存失败: " + e.getMessage());
                rollback(c);
            }
        }
        return var;
    }


    private void commit(Connection c) {
        if (c != null && !autoCommit) {
            try {
                c.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private void rollback(Connection c) {
        if (c != null && !autoCommit) {
            try {
                c.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
