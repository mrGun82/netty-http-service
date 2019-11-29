package com.eding.framework.config;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-29 09:35
 */

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class AbstractConfigLoader {

    private static ClassLoader loader = ClassLoader.getSystemClassLoader();

    @Getter
    private static AppConfig appConfig;

    @Getter
    private static Yaml yaml;

    public AbstractConfigLoader() {
        if (Objects.nonNull(appConfig)) {
            return;
        }
        loadConfigFile();

    }

    private void loadConfigFile() {
        if (Objects.nonNull(yaml)) {
            return;
        }
        yaml = new Yaml();
        List<AppConfigFiles> configFiles = AppConfigFiles.getConfigFiles();
        for (AppConfigFiles cf : configFiles) {
            log.info("load confile file: " + cf);
            InputStream in = null;
            File f = new File(cf.getPath());
            if (f.exists()) {
                try {
                    in = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                in = loader.getResourceAsStream(cf.getPath());
            }
            if (Objects.nonNull(in)) {
                appConfig = yaml.loadAs(in, AppConfig.class);
                return;
            }
        }
        if (Objects.nonNull(appConfig)) {
            throw new RuntimeException("配置文件获取失败");
        }
    }
}