package com.dubbo.hystrix;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see com.netflix.hystrix.HystrixThreadPoolProperties
 * @see "https://github.com/Netflix/Hystrix/wiki/Configuration"
 * @see "http://www.jianshu.com/p/39763a0bd9b8"
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DubboHystrixThreadPoolProperties {
    // 所有属性默认-1，代表不设置，只有非负数才有效
    int coreSize() default -1;

    int maximumSize() default -1;

    int keepAliveTimeMinutes() default -1;

    int maxQueueSize() default -1;

    int queueSizeRejectionThreshold() default -1;

    boolean allowMaximumSizeToDivergeFromCoreSize() default false;

    int rollingStatisticalWindowInMilliseconds() default -1;

    int rollingStatisticalWindowBuckets() default -1;
}
