package com.dubbo.hystrix;

/**
 * @author liujiawei
 * @create 2017-12-19 上午12:25
 * <p>
 * 熔断器生成工厂接口，实现类配合 AbstractHystrixConfigBuilder使用，逻辑参见 DubboHystrixAutoConfiguration
 * <p>
 * 重要:工厂实现类beanName 必须为hystrixConfigBuilderFactory
 */
public interface HystrixConfigBuilderFactory {
    AbstractHystrixConfigBuilder getHystrixConfigBuilder();
}
