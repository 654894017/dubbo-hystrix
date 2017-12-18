package com.dubbo.hystrix;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * dubbo hystrix 加载配置
 *
 * @author liujiawei
 * @create 2017-12-18 上午10:29
 */
@Configuration
@EnableConfigurationProperties(HystrixProperties.class)
@ConditionalOnClass(HystrixConfigBuilder.class)
public class DubboHystrixAutoConfiguration {
    @Bean
    public HystrixConfigBuilder hystrixConfigBuilder() {
        return new HystrixConfigBuilder();
    }

}
