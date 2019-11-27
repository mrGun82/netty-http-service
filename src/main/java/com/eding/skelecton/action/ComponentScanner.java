package com.eding.skelecton.action;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 14:13
 */
@Slf4j
public abstract class ComponentScanner {

    public ComponentScanner() {
    }

    public void scanPackage(Class<?> clazz) throws Exception {
        scanPackage(clazz.getPackage().getName());
    }

    public void scanPackage(String packageName) throws Exception {
        String packagePath = packageName.replace(".", "/");
        log.info("scan package path: " + packagePath);
        try {
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader()
                    .getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url.getProtocol().equals("jar")) {
                    parse(url);
                } else {
                    File file = new File(url.toURI());
                    if (file.exists()) {
                        parse(file, packageName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public abstract void dealClass(Class<?> clazz) throws Exception;

    private void parse(URL url) throws Exception {
        Enumeration<JarEntry> jarEntries = ((JarURLConnection) url.openConnection())
                .getJarFile().entries();

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarName = jarEntry.getName();

            if (!jarEntry.isDirectory() && jarName.endsWith(".class")) {
                dealClassName(jarName.replace("/", ".").replace(".class", ""));
            }
        }
    }

    private void parse(File curFile, String packageName) throws Exception {
        File[] fileList = curFile.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getName().endsWith(".class");
            }
        });

        for (File file : fileList) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                parse(file, packageName + "." + fileName);
            } else {
                String className = packageName + "." + fileName.replace(".class", "");
                dealClassName(className);
            }
        }
    }

    private void dealClassName(String className) throws Exception {
        try {
            Class<?> clazz = Class.forName(className);
            if (!clazz.isAnnotation()
                    && !clazz.isInterface()
                    && !clazz.isEnum()
                    && !clazz.isPrimitive()) {
                dealClass(clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
