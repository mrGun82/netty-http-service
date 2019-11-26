package com.eding.skelecton.config;

import com.eding.skelecton.action.ActionFactory;
import lombok.Getter;

import java.util.Objects;

/**
 * @program:http-service
 * @description:
 * @author:jiagang
 * @create 2019-11-26 09:36
 */
public class ActionConfigLoader extends AbstractConfigLoader implements ConfigLoader {

    @Getter
    private Integer servicePort;
    @Getter
    private String actionPackage;

    @Override
    public void loadConfig() {
        if (Objects.isNull(getAppConfig())) {
            throw new RuntimeException("配置文件获取失败");
        }
        servicePort = Integer.parseInt(getAppConfig().getServer().get("port"));

        if (Objects.isNull(servicePort) || servicePort < 1) {
            throw new RuntimeException("服务无法启动 端口: " + servicePort);
        }

        actionPackage = getAppConfig().getPackages().get("action");
        if (Objects.isNull(actionPackage)) {
            throw new RuntimeException("包名无法扫描: " + servicePort);
        }

    }
}
