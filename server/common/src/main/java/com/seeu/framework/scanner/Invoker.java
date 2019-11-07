package com.seeu.framework.scanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker {
    /**
     * 方法
     */
    private Method method;

    /**
     * 目标对象
     */
    private Object target;

    private String proto;

    private boolean isClient = false;

    public static Invoker valueOf(Method method, Object target, String proto){
        Invoker invoker = new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        invoker.setProto(proto);
        return invoker;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public static Invoker valueOf(Method method, Object target, String proto, boolean isClient){
        Invoker invoker = new Invoker();
        invoker.setMethod(method);
        invoker.setTarget(target);
        invoker.setProto(proto);
        invoker.setClient(isClient);
        return invoker;
    }

    /**
     * 执行
     * @param paramValues
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public Object invoke(Object... paramValues){
        try {
            if(isClient) {

            } else {
                return method.invoke(target, paramValues);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }
}
