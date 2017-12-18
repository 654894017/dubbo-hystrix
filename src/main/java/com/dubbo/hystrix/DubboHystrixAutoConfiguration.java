package com.dubbo.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
    @ConditionalOnBean(name = "hystrixConfigBuilderFactory")
    public AbstractHystrixConfigBuilder hystrixConfigBuilder() {
        HystrixConfigBuilderFactory factory = (HystrixConfigBuilderFactory) context.getBean("hystrixConfigBuilderFactory");
        return factory.getHystrixConfigBuilder();
    }

    @Bean
    @ConditionalOnMissingBean(name = "hystrixConfigBuilder")
    public AbstractHystrixConfigBuilder defaultHystrixConfigBuilder() {
        return new DefaultHystrixConfigBuilder();
    }
}
