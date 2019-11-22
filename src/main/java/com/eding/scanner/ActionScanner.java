package com.eding.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;

/**
 * @program:eding-cloud
 * @description:
 * @author:jiagang
 * @create 2019-11-22 14:13
 */
public abstract class ActionScanner {

    public ActionScanner() {
    }

    // scanPackage方法的重载
    public void scanPackage(Class<?> clazz) throws Exception {
        scanPackage(clazz.getPackage().getName());
    }

    public void scanPackage(String packageName) throws Exception {
        // 包名称转换为路径名
        String packagePath = packageName.replace(".", "/");

        try {
            // 类加载器得到URL的枚举
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader()
                    .getResources(packagePath);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                // 处理jar包
                if (url.getProtocol().equals("jar")) {
                    parse(url);
                } else {
                    File file = new File(url.toURI());
                    if (file.exists()) {
                        // 处理包
                        parse(file, packageName);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // 抽象方法，由用户自行处理扫描到的类
    public abstract void dealClass(Class<?> clazz) throws Exception;

    // jar包的扫描
    private void parse(URL url) throws Exception {
        Enumeration<JarEntry> jarEntries = ((JarURLConnection) url.openConnection())
                .getJarFile().entries();

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarName = jarEntry.getName();

            if (!jarEntry.isDirectory() && jarName.endsWith(".class")) {
                // 将文件路径名转换为包名称的形式
                dealClassName(jarName.replace("/", ".").replace(".class", ""));
            }
        }
    }

    // 包扫描
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

    // 将找到的class文件生成类对象
    private void dealClassName(String className) throws Exception {
        try {
            Class<?> clazz = Class.forName(className);
            // 注解、接口、枚举、原始类型不处理
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
