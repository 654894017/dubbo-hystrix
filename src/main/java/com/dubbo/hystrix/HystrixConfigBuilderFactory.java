package com.dubbo.hystrix;

/**
 * @author liujiawei
 * @create 2017-12-19 上午12:25
 */
public interface HystrixConfigBuilderFactory {
    AbstractHystrixConfigBuilder getHystrixConfigBuilder();
}
