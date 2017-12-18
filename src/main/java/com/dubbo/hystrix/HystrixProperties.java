package com.dubbo.hystrix;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @see "https://github.com/Netflix/Hystrix/wiki/Configuration"
 * @see "http://www.jianshu.com/p/39763a0bd9b8"
 */
@Data
@ConfigurationProperties(prefix = "pandora.hystrix.global")
public class HystrixProperties {
    /* null means it hasn't been overridden */
    private Boolean circuitBreakerEnabled;
    private Integer circuitBreakerErrorThresholdPercentage;
    private Boolean circuitBreakerForceClosed;
    private Boolean circuitBreakerForceOpen;
    private Integer circuitBreakerRequestVolumeThreshold;
    private Integer circuitBreakerSleepWindowInMilliseconds;
    private Integer executionTimeoutInMilliseconds;
    //建议使用dubbo的超时，禁用这里的超时，设置为false
    private Boolean executionTimeoutEnabled;
    private Boolean fallbackEnabled;
    private Integer metricsRollingStatisticalWindowInMilliseconds;
    private Integer metricsRollingStatisticalWindowBuckets;
}
