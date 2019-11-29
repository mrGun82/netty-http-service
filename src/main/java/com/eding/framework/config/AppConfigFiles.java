package com.eding.framework.config;

import lombok.ToString;

import java.io.File;
import java.util.*;

@ToString
public enum AppConfigFiles {

    APPLICATION_EXTERNAL((System.getProperty("user.dir")).substring(0, (System.getProperty("user.dir").lastIndexOf(File.separator))) + File.separator + "config" + File.separator + "application.yml", 1),
    APPLICATION_INTERNAL("application.yml", 2);

    private String path;
    private int level;

    AppConfigFiles(String path, int level) {
        this.path = path;
        this.level = level;
    }

    public static List<AppConfigFiles> getConfigFiles() {
        List<AppConfigFiles> queue = new LinkedList<AppConfigFiles>();
        for (AppConfigFiles acf : AppConfigFiles.values()) {
            queue.add(acf);
        }
        Collections.sort(queue, new Comparator<AppConfigFiles>() {
                    public int compare(AppConfigFiles fileA, AppConfigFiles fileB) {
                        return fileA.level - fileB.level;
                    }
                }
        );
        return queue;
    }

    public String getPath() {
        return path;
    }
}
