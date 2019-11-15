package com.seeu.framework.scanner;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.seeu.framework.rpc.RpcMsg.ServerType;
import com.seeu.proto.Discover;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcInvokerHolder {

    private static Logger logger = LoggerFactory.getLogger(RpcInvokerHolder.class);

    /**
     * 命令调用器缓存
     */

    private static Map<ServerType, Map<String, Invoker>> invokerServiceMap = new HashMap<>();
    private static Map<ServerType, Map<String, Invoker>> invokerClientMap = new HashMap<>();
    private static Map<ServerType, Map<String, Method>> clientMethod = new HashMap<>();
    private static Map<ServerType, Map<String, Method>> serverMethod = new HashMap<>();
    private static ClassLoader classLoader;


    /**
     * 添加命令调用
     */
    public static void addServiceInvoker(ServerType type, String method, Invoker invoker) {
        Map<String, Invoker> map = invokerServiceMap.computeIfAbsent(type, k -> new HashMap<>());
        map.put(method, invoker);
    }


    /**
     * 获取命令调用
     */
    public static Invoker getServiceInvoker(ServerType type, String method) {
        Map<String, Invoker> map = invokerServiceMap.get(type);
        if (null == map) {
            return null;
        }

        return map.get(method);
    }

    public static Object serviceInvoke(ServerType type, String method, ByteString proto) {
        Invoker invoker = getServiceInvoker(type, method);
        if (null == invoker) {
            logger.error("serviceInvoke {},{} but can't find any invoker!", type, method);
            return null;
        }

        try {
            Map<String, Method> methodMap = serverMethod
                .computeIfAbsent(type, k -> new HashMap<>());
            Method mth = methodMap.get(method);
            if (null == mth) {
                if(null == classLoader) {
                    classLoader = RpcInvokerHolder.class.getClassLoader();
                }

                Class<?> cls;
                try {
                    cls = classLoader.loadClass("com.seeu.proto." + invoker.getProto());
                } catch (ClassNotFoundException e) {
                    String className = "com.seeu.proto." + invoker.getProto().replace('.', '$');
                    cls = classLoader.loadClass(className);
                }
                mth = cls.getMethod("parseFrom", ByteString.class);
                methodMap.put(method, mth);
            }

            MessageLite mlite = (MessageLite) mth.invoke(null, proto);

            return invoker.invoke(mlite);
        } catch (Exception e) {
            logger.error("serviceInvoke {}, proto: {} catch an error: ", method, invoker.getProto(),
                e);
            return null;
        }
    }

    /**
     * 添加命令调用
     */
    public static void addClientInvoker(ServerType type, String method, Invoker invoker) {
        Map<String, Invoker> map = invokerClientMap.computeIfAbsent(type, k -> new HashMap<>());
        map.put(method, invoker);
    }

    /**
     * 获取命令调用
     */
    public static Invoker getClientInvoker(ServerType type, String method) {
        Map<String, Invoker> map = invokerClientMap.get(type);
        if (null == map) {
            return null;
        }

        return map.get(method);
    }

    public static Object clientInvoke(ServerType type, String method, ByteString proto) {
        Invoker invoker = getClientInvoker(type, method);
        if (null == invoker) {
            logger.error("clientInvoke {}, {} but can't find any invoker!", type, method);
            return null;
        }

        try {
            Map<String, Method> methodMap = clientMethod
                .computeIfAbsent(type, k -> new HashMap<>());
            Method mth = methodMap.get(method);
            if (null == mth) {
                if(null == classLoader) {
                    classLoader = RpcInvokerHolder.class.getClassLoader();
                }

                Class<?> cls;
                try {
                    cls = classLoader.loadClass("com.seeu.proto." + invoker.getProto());
                } catch (ClassNotFoundException e) {
                    String className = "com.seeu.proto." + invoker.getProto().replace('.', '$');
                    cls = classLoader.loadClass(className);
                }

                mth = cls.getMethod("parseFrom", ByteString.class);
                methodMap.put(method, mth);
            }

            MessageLite mlite = (MessageLite) mth.invoke(null, proto);

            return invoker.invoke(mlite);
        } catch (Exception e) {
            logger.error("clientInvoke {}, proto: {} catch an error: ", method, invoker.getProto(),
                e);
            return null;
        }
    }
}
