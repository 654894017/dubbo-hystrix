package com.dubbo.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dubbo hystrix 加载配置
 *
 * @author liujiawei
 * @create 2017-12-18 上午10:29
 */
@Configuration
@ConditionalOnClass({AbstractHystrixConfigBuilder.class, DefaultHystrixConfigBuilder.class, HystrixConfigBuilderFactory.class})
public class DubboHystrixAutoConfiguration {

    @Autowired
    private ApplicationContext context;

    @Bean
    public AbstractHystrixConfigBuilder defaultHystrixConfigBuilder() {
        HystrixConfigBuilderFactory factory = context.getBean(HystrixConfigBuilderFactory.class);
        return factory != null ? factory.getHystrixConfigBuilder() : new DefaultHystrixConfigBuilder();
    }

}
