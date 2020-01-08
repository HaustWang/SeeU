package com.seeu.framework.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public class PackageUtil {
    /**
     * jar中的文件路径分隔符
     */
    private static final char SLASH_CHAR = '/';
    /**
     * 包名分隔符
     */
    private static final char DOT_CHAR = '.';
    private static Map<String, Set<Class<?>>> packageMap = new LinkedHashMap<>();

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 在当前项目中递归寻找指定包下的所有类
     *
     * @param packageName 用'.'分隔的包名
     * @return 该包名下的所有类
     */
    public static Set<Class<?>> getClasses(String packageName) {
        return getClasses(packageName, true);
    }

    /**
     * 在当前项目中寻找指定包下的所有类
     *
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @return 该包名下的所有类
     */
    public static Set<Class<?>> getClasses(String packageName, boolean recursive) {
        // 第一个class类的集合
        if (null != packageMap.get(packageName))
            return packageMap.get(packageName);

        Set<Class<?>> classes = new LinkedHashSet<>();
        packageMap.put(packageName, classes);

        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace(DOT_CHAR, File.separatorChar);
        log.debug("resources path: {}, {}", ClassLoader.getSystemResource(""), getClassLoader().getResource(""));
        try {
            //获取当前线程的类装载器中相应包名对应的资源
//            Enumeration<URL> dirs = ClassLoader.getSystemResources(packageDirName);
            Enumeration<URL> dirs = getClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                //获取元素协议名称，主要处理jar包和file类型
                String protocol = url.getProtocol();
                switch (protocol) {
                    case "file":
                        getClassInFile(url, packageName, recursive, classes);
                        break;
                    case "jar":
                        getClassInJar(url, packageName, recursive, classes);
                        break;
                    default:
                        //在某些WEB服务器中运行WAR包时，它不会像TOMCAT一样将WAR包解压为目录的，如JBOSS7，它是使用了一种叫VFS的协议
                        log.error("unknown protocol " + protocol);
                        break;
                }
            }
        } catch (IOException e) {
            log.error("catch an exception:", e);
        }
        return classes;
    }

    /**
     * 在给定的文件或文件夹中寻找指定包下的所有类
     *
     * @param url         包的统一资源定位符
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @param classes     返回类的集合
     * @return 该包名下的所有类
     */
    public static void getClassInFile(URL url, String packageName, boolean recursive, Set<Class<?>> classes) {
        try {
            String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
            getClassInFile(filePath, packageName, recursive, classes);
        } catch (Exception e) {
            log.error("catch an exception:", e);
        }
    }

    /**
     * 在给定的文件或文件夹中寻找指定包下的所有类
     *
     * @param packagePath 包的路径
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @param classes     返回类的集合
     * @return 该包名下的所有类
     */
    public static void getClassInFile(String packagePath, String packageName, boolean recursive, Set<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                getClassInFile(file.getAbsolutePath(), packageName + "." + file.getName(), recursive, classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // classes.add(Class.forName(packageName + '.' + className));
                    // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(getClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    log.error("catch an exception:", e);
                }
            }
        }
    }

    /**
     * 在给定的jar包中寻找指定包下的所有类
     *
     * @param filePath    包的路径
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @param classes     返回类的集合
     * @return 该包名下的所有类
     */
    public static void getClassInJar(String filePath, String packageName, boolean recursive, Set<Class<?>> classes) {
        try {
            JarFile jar = new JarFile(filePath);
            getClassInJar(jar, packageName, recursive, classes);
        } catch (IOException e) {
            log.error("catch an exception:", e);
        }
    }

    /**
     * 在给定的jar包中寻找指定包下的所有类
     *
     * @param url         jar包的统一资源定位符
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @param classes     返回类的集合
     * @return 该包名下的所有类
     */
    public static void getClassInJar(URL url, String packageName, boolean recursive, Set<Class<?>> classes) {
        try {
            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
            getClassInJar(jar, packageName, recursive, classes);
        } catch (IOException e) {
            log.error("catch an exception:", e);
        }
    }

    /**
     * 在给定的jar包中寻找指定包下的所有类
     *
     * @param jar         jar对象
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @param classes     返回类的集合
     * @return 该包名下的所有类
     */
    public static void getClassInJar(JarFile jar, String packageName, boolean recursive, Set<Class<?>> classes) {
        // 从此jar包 得到一个枚举类
        Enumeration<JarEntry> entries = jar.entries();
        // 获取包的名字 并进行替换
        String packageDirName = packageName.replace(DOT_CHAR, File.separatorChar);
        // 同样的进行循环迭代
        while (entries.hasMoreElements()) {
            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            // 如果是以/开头的
            if (name.charAt(0) == '/') {
                // 获取后面的字符串
                name = name.substring(1);
            }
            // 如果前半部分和定义的包名相同
            if (name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                // 如果以"/"结尾 是一个包
                if (idx != -1) {
                    // 获取包名 把"/"替换成"."
                    packageName = name.substring(0, idx).replace(File.separatorChar, DOT_CHAR);
                }
                // 如果可以迭代下去 并且是一个包
                if ((idx != -1) || recursive) {
                    // 如果是一个.class文件 而且不是目录
                    if (name.endsWith(".class") && !entry.isDirectory()) {
                        // 去掉后面的".class" 获取真正的类名
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        try {
                            // 添加到classes
//                            classes.add(Class.forName(packageName + '.' + className));
                            classes.add(getClassLoader().loadClass(packageName + "." + className));
                        } catch (ClassNotFoundException e) {
                            log.error("catch an exception:", e);
                        }
                    }
                }
            }
        }
    }
}
