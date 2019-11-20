package com.seeu.framework.scanner;

import com.seeu.framework.annotations.RpcMethod;
import com.seeu.framework.annotations.RpcService;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RpcScanner implements BeanPostProcessor {

    Logger logger = LoggerFactory.getLogger(RpcScanner.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
        throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
        throws BeansException {

        Class<? extends Object> clazz = bean.getClass();

        //判断是否为handler接口类
        RpcService rpcService = clazz.getAnnotation(RpcService.class);
        if (rpcService != null) {
            //找出命令方法
            Method[] methods = clazz.getMethods();
            if (methods != null && methods.length > 0) {
                for (Method method : methods) {
                    RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
                    if (rpcMethod == null) {
                        continue;
                    }

                    final String methodName = rpcMethod.method();

                    if (RpcInvokerHolder.getServiceInvoker(rpcMethod.type(), methodName) == null) {
                        RpcInvokerHolder
                            .addServiceInvoker(rpcMethod.type(), methodName,
                                Invoker.valueOf(method, bean, rpcMethod.proto()));
                    } else {
                        logger.warn("repeated service method {}", methodName);
                    }
                }
            }
        }
        return bean;
    }
}
