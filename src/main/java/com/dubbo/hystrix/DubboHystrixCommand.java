package com.dubbo.hystrix;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Dubbo Hystrix Command
 *
 * @author liujiawei
 * @create 2017-12-15 下午11:23
 */
public class DubboHystrixCommand extends HystrixCommand<Result> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboHystrixCommand.class);
    private Invoker<?> invoker;
    private Invocation invocation;
    private Method method;
    private String fallbackClass;
    @Getter
    private RpcContext rpcContext;

    DubboHystrixCommand(Invoker invoker, Invocation invocation, Method method, HystrixCommandProperties.Setter commandProperties, HystrixThreadPoolProperties.Setter threadPoolProperties, HystrixMethodConfig config) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(config.getGroupKey()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(config.getCommandKey()))
                .andCommandPropertiesDefaults(commandProperties)
                .andThreadPoolPropertiesDefaults(threadPoolProperties));
        this.invoker = invoker;
        this.invocation = invocation;
        this.fallbackClass = config.getFallbackClass();
        this.method = method;
        //保存当前线程的rpc上下文
        this.rpcContext = RpcContext.getContext();
    }

    /**
     * 在hystrix线程池中执行
     *
     * @return dubbo执行结果
     * @throws RpcException dubbo异常
     */
    protected Result run() throws RpcException {
        //把主线程的上下文copy过来
        BeanUtils.copyProperties(rpcContext, RpcContext.getContext());
        try {
            return invoker.invoke(invocation);
        } finally {
            //拿到dubbo执行完的最新的上下文
            BeanUtils.copyProperties(RpcContext.getContext(), rpcContext);
            RpcContext.removeContext();
        }
    }

    @Override
    protected Result getFallback() {
        //把主线程的上下文copy过来
        BeanUtils.copyProperties(rpcContext, RpcContext.getContext());
        RpcResult result = new RpcResult();
        try {
            if (StringUtils.isNotBlank(fallbackClass)) {
                Class<?> clz = Class.forName(fallbackClass);
                if (!invoker.getInterface().isAssignableFrom(clz)) {
                    // fallback类需要是dubbo接口的实现类
                    LOGGER.warn("DubboHystrixCommand getFallback fallbackClass:{} must impl of interface:{}"
                            , fallbackClass
                            , invoker.getInterface().getName());
                    return result;
                }
                //优先获取spring中的对象
                Object obj = AbstractHystrixConfigBuilder.getContext().getBean(clz);
                if (obj == null) {
                    obj = clz.newInstance();
                }
                Method fallbackMethod = clz.getMethod(method.getName(), method.getParameterTypes());
                if (!fallbackMethod.isAccessible()) {
                    fallbackMethod.setAccessible(true);
                }
                result.setValue(fallbackMethod.invoke(obj, invocation.getArguments()));
            }
        } catch (Exception e) {
            LOGGER.error("DubboHystrixCommand getFallback error fallbackClass:{},fallbackMethod:{},arguments:{}"
                    , fallbackClass
                    , method.getName()
                    , Arrays.toString(invocation.getArguments())
                    , e);
        } finally {
            //拿到执行完的最新的上下文
            BeanUtils.copyProperties(RpcContext.getContext(), rpcContext);
            RpcContext.removeContext();
        }
        return result;
    }
}
