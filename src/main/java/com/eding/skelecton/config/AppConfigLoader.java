package com.eding.skelecton.config;

import lombok.Getter;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-25 17:26
 */
public final class AppConfigLoader implements ConfigLoader {

    private static AppConfigLoader appConfigLoader = new AppConfigLoader();
    @Getter
    private ServerConfigLoader serverConfigLoader;
    @Getter
    private DatabaseConfigLoader databaseConfigLoader;

    private AppConfigLoader() throws RuntimeException {
        serverConfigLoader = new ServerConfigLoader();
        databaseConfigLoader = new DatabaseConfigLoader();
    }

    @Override
    public void loadConfig() {
        serverConfigLoader.loadConfig();
        databaseConfigLoader.loadConfig();
    }


    public static AppConfigLoader getInstance() {
        return appConfigLoader;
    }
}
