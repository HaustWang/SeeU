package com.seeu.framework.scanner;

import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
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

    private static Map<String, Invoker> invokerServiceMap = new HashMap<>();
    private static Map<String, Invoker> invokerClientMap = new HashMap<>();
    private static Map<String, Method> clientMethod = new HashMap<>();
    private static Map<String, Method> serverMethod = new HashMap<>();


    /**
     * 添加命令调用
     */
    public static void addServiceInvoker(String method, Invoker invoker) {
        invokerServiceMap.put(method, invoker);
    }


    /**
     * 获取命令调用
     */
    public static Invoker getServiceInvoker(String method) {
        return invokerServiceMap.get(method);
    }

    public static Object serviceInvoke(String method, ByteString proto) {
        Invoker invoker = getServiceInvoker(method);
        if (null == invoker) {
            logger.error("serviceInvoke {} but can't find any invoker!", method);
            return null;
        }

        try {
            Method mth = serverMethod.get(method);
            if(null == mth) {
                Class<?> cls = RpcInvokerHolder.class.getClassLoader().loadClass("com.seeu.proto." + invoker.getProto());
                mth = cls.getMethod("parseFrom", ByteString.class);
                serverMethod.put(method, mth);
            }

            MessageLite mlite = (MessageLite) mth.invoke(proto);

            return invoker.invoke(mlite);
        } catch (Exception e){
            logger.error("serviceInvoke {}, proto: {} catch an error: ", method, invoker.getProto(), e);
            return null;
        }
    }

    /**
     * 添加命令调用
     */
    public static void addClientInvoker(String method, Invoker invoker) {
        invokerClientMap.put(method, invoker);
    }

    /**
     * 获取命令调用
     */
    public static Invoker getClientInvoker(String method) {
        return invokerClientMap.get(method);
    }

    public static Object clientInvoke(String method, ByteString proto) {
        Invoker invoker = getClientInvoker(method);
        if (null == invoker) {
            logger.error("clientInvoke {} but can't find any invoker!", method);
            return null;
        }

        try {
            Method mth = clientMethod.get(method);
            if(null == mth) {
                Class<?> cls = RpcInvokerHolder.class.getClassLoader().loadClass("com.seeu.proto." + invoker.getProto());
                mth = cls.getMethod("parseFrom", ByteString.class);
                clientMethod.put(method, mth);
            }

            MessageLite mlite = (MessageLite) mth.invoke(proto);

            return invoker.invoke(mlite);
        } catch (Exception e){
            logger.error("serviceInvoke {}, proto: {} catch an error: ", method, invoker.getProto(), e);
            return null;
        }
    }
}
