package com.dubbo.hystrix;

import org.springframework.core.env.Environment;

/**
 * @author liujiawei
 * @create 2017-12-19 上午12:23
 */
public class DefaultHystrixConfigBuilder extends AbstractHystrixConfigBuilder {
    @Override
    protected <T> T getProperty(String classSimpleName, String methodName, String name, Class<T> type) {
        Environment env = context.getEnvironment();
        return env.getProperty(String.format(PROPERTY_FORMAT, classSimpleName, methodName, name), type);
    }
}
