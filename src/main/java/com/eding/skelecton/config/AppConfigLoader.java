package com.eding.skelecton.config;

import com.eding.skelecton.action.ActionFactory;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-25 17:26
 */
public final class AppConfigLoader implements ConfigLoader {

    private static AppConfigLoader appConfigLoader = new AppConfigLoader();
    @Getter
    private ActionConfigLoader actionConfigLoader;
    @Getter
    private DatabaseConfigLoader databaseConfigLoader;

    private AppConfigLoader() throws RuntimeException {
        actionConfigLoader = new ActionConfigLoader();
        databaseConfigLoader = new DatabaseConfigLoader();
    }

    @Override
    public void loadConfig() {
        actionConfigLoader.loadConfig();
        databaseConfigLoader.loadConfig();
    }


    public static AppConfigLoader getInstance() {
        return appConfigLoader;
    }
}
