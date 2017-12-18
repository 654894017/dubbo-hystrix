package com.dubbo.hystrix;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Dubbo Hystrix 过滤器
 *
 * @author liujiawei
 * @create 2017-12-15 下午11:21
 */
public class DubboHystrixFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(DubboHystrixFilter.class);

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            //消费者方生效
            if (RpcContext.getContext().isConsumerSide()) {
                Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                HystrixMethodConfig hystrixConfig = method.getAnnotation(HystrixMethodConfig.class);
                if (hystrixConfig != null) {
                    HystrixCommandProperties.Setter commandProperties = this.buildCommandProperties();
                    HystrixThreadPoolProperties.Setter threadPoolProperties = this.buildThreadPoolProperties(hystrixConfig.threadPoolProperties());
                    DubboHystrixCommand command = new DubboHystrixCommand(invoker, invocation, method, commandProperties, threadPoolProperties, hystrixConfig);
                    Result result = command.execute();
                    //把执行完的rpc上下文copy出来,放到调用线程的threadLocal中
                    BeanUtils.copyProperties(command.getRpcContext(), RpcContext.getContext());
                    return result;
                }
            }
        } catch (NoSuchMethodException e) {
            LOGGER.error("DubboHystrixFilter error NoSuchMethod interface:{} methodName:{} parameterTypes:{}"
                    , invoker.getInterface().getName()
                    , invocation.getMethodName()
                    , Arrays.toString(invocation.getParameterTypes())
                    , e);
        }
        return invoker.invoke(invocation);
    }

    private HystrixCommandProperties.Setter buildCommandProperties() {
        HystrixProperties properties = ServiceBean.getSpringContext().getBean(HystrixProperties.class);
        HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter();
        BeanUtils.copyProperties(properties, setter);
        return setter;
    }

    private HystrixThreadPoolProperties.Setter buildThreadPoolProperties(DubboHystrixThreadPoolProperties poolProperties) {
        HystrixThreadPoolProperties.Setter setter = HystrixThreadPoolProperties.Setter()
                .withAllowMaximumSizeToDivergeFromCoreSize(poolProperties.allowMaximumSizeToDivergeFromCoreSize());
        //不设置则取默认值 com.netflix.hystrix.HystrixThreadPoolProperties default_* 属性
        if (poolProperties.coreSize() > -1) {
            setter.withCoreSize(poolProperties.coreSize());
        }
        if (poolProperties.keepAliveTimeMinutes() > -1) {
            setter.withKeepAliveTimeMinutes(poolProperties.keepAliveTimeMinutes());
        }
        if (poolProperties.maximumSize() > -1) {
            setter.withMaximumSize(poolProperties.maximumSize());
        }
        if (poolProperties.maxQueueSize() > -1) {
            setter.withMaxQueueSize(poolProperties.maxQueueSize());
        }
        if (poolProperties.queueSizeRejectionThreshold() > -1) {
            setter.withQueueSizeRejectionThreshold(poolProperties.queueSizeRejectionThreshold());
        }
        if (poolProperties.rollingStatisticalWindowBuckets() > -1) {
            setter.withMetricsRollingStatisticalWindowBuckets(poolProperties.rollingStatisticalWindowBuckets());
        }
        if (poolProperties.rollingStatisticalWindowInMilliseconds() > -1) {
            setter.withMetricsRollingStatisticalWindowInMilliseconds(poolProperties.rollingStatisticalWindowInMilliseconds());
        }
        return setter;
    }
}
