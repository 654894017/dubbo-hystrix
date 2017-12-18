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
    private Integer executionIsolationSemaphoreMaxConcurrentRequests;
    private Boolean executionIsolationThreadInterruptOnTimeout;
    private Boolean executionIsolationThreadInterruptOnFutureCancel;
    private Integer executionTimeoutInMilliseconds;
    //使用dubbo的超时，禁用这里的超时
    private Boolean executionTimeoutEnabled = false;
    private Integer fallbackIsolationSemaphoreMaxConcurrentRequests;
    private Boolean fallbackEnabled;
    private Integer metricsHealthSnapshotIntervalInMilliseconds;
    private Integer metricsRollingPercentileBucketSize;
    private Boolean metricsRollingPercentileEnabled;
    private Integer metricsRollingPercentileWindowInMilliseconds;
    private Integer metricsRollingPercentileWindowBuckets;
    private Integer metricsRollingStatisticalWindowInMilliseconds;
    private Integer metricsRollingStatisticalWindowBuckets;
    private Boolean requestCacheEnabled;
    private Boolean requestLogEnabled;
}
