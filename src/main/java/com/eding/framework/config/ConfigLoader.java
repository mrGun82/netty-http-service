package com.eding.framework.config;

@FunctionalInterface
public interface ConfigLoader {
    void loadConfig() throws RuntimeException;
}
