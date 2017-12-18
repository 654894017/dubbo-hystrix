package com.dubbo.hystrix;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

/**
 * 针对不同方法创建hystrix配置
 *
 * @author liujiawei
 * @create 2017-12-18 下午1:30
 */
public class HystrixConfigBuilder implements ApplicationContextAware {
    private static final String FORMAT = "pandora.hystrix.%s.%s.%s";
    private static final String CORE_SIZE = "coreSize";
    private static final String MAXIMUM_SIZE = "maximumSize";
    private static final String KEEP_ALIVE_TIME_MINUTES = "keepAliveTimeMinutes";
    private static final String MAX_QUEUE_SIZE = "maxQueueSize";
    private static final String QUEUE_SIZE_REJECTION_THRESHOLD = "queueSizeRejectionThreshold";
    private static final String ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS = "rollingStatisticalWindowInMilliseconds";
    private static final String ROLLING_STATISTICAL_WINDOW_BUCKETS = "rollingStatisticalWindowBuckets";
    private static final String ALLOW_MAXIMUM_SIZE_TO_DIVERGE_FROM_CORE_SIZE = "allowMaximumSizeToDivergeFromCoreSize";
    private static final String FALLBACK_CLASS = "fallbackClass";

    private static ApplicationContext context;

    public HystrixCommandProperties.Setter buildCommandProperties() {
        HystrixProperties properties = context.getBean(HystrixProperties.class);
        HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter();
        if (properties.getCircuitBreakerEnabled() != null) {
            setter.withCircuitBreakerEnabled(properties.getCircuitBreakerEnabled());
        }
        if (properties.getCircuitBreakerErrorThresholdPercentage() != null) {
            setter.withCircuitBreakerErrorThresholdPercentage(properties.getCircuitBreakerErrorThresholdPercentage());
        }
        if (properties.getCircuitBreakerForceClosed() != null) {
            setter.withCircuitBreakerForceClosed(properties.getCircuitBreakerForceClosed());
        }
        if (properties.getCircuitBreakerForceOpen() != null) {
            setter.withCircuitBreakerForceOpen(properties.getCircuitBreakerForceOpen());
        }
        if (properties.getMetricsRollingStatisticalWindowBuckets() != null) {
            setter.withMetricsRollingStatisticalWindowBuckets(properties.getMetricsRollingStatisticalWindowBuckets());
        }
        if (properties.getMetricsRollingStatisticalWindowInMilliseconds() != null) {
            setter.withMetricsRollingStatisticalWindowInMilliseconds(properties.getMetricsRollingStatisticalWindowInMilliseconds());
        }
        if (properties.getFallbackEnabled() != null) {
            setter.withFallbackEnabled(properties.getFallbackEnabled());
        }
        if (properties.getExecutionTimeoutEnabled() != null) {
            setter.withExecutionTimeoutEnabled(properties.getExecutionTimeoutEnabled());
        }
        if (properties.getExecutionTimeoutInMilliseconds() != null) {
            setter.withExecutionTimeoutInMilliseconds(properties.getExecutionTimeoutInMilliseconds());
        }
        if (properties.getCircuitBreakerSleepWindowInMilliseconds() != null) {
            setter.withCircuitBreakerSleepWindowInMilliseconds(properties.getCircuitBreakerSleepWindowInMilliseconds());
        }
        if (properties.getCircuitBreakerRequestVolumeThreshold() != null) {
            setter.withCircuitBreakerRequestVolumeThreshold(properties.getCircuitBreakerRequestVolumeThreshold());
        }
        return setter;
    }

    /**
     * @param methodName      方法名
     * @param classSimpleName dubbo接口类名
     * @return
     */
    public HystrixThreadPoolProperties.Setter buildThreadPoolProperties(String classSimpleName, String methodName) {
        HystrixThreadPoolProperties.Setter setter = HystrixThreadPoolProperties.Setter();
        Integer coreSize = getProperty(classSimpleName, methodName, CORE_SIZE, Integer.class);
        if (coreSize != null) {
            setter.withCoreSize(coreSize);
        }
        Integer maximumSize = getProperty(classSimpleName, methodName, MAXIMUM_SIZE, Integer.class);
        if (maximumSize != null) {
            setter.withMaximumSize(maximumSize);
        }
        Integer keepAliveTimeMinutes = getProperty(classSimpleName, methodName, KEEP_ALIVE_TIME_MINUTES, Integer.class);
        if (keepAliveTimeMinutes != null) {
            setter.withKeepAliveTimeMinutes(keepAliveTimeMinutes);
        }
        Integer maxQueueSize = getProperty(classSimpleName, methodName, MAX_QUEUE_SIZE, Integer.class);
        if (maxQueueSize != null) {
            setter.withMaxQueueSize(maxQueueSize);
        }
        Integer queueSizeRejectionThreshold = getProperty(classSimpleName, methodName, QUEUE_SIZE_REJECTION_THRESHOLD, Integer.class);
        if (queueSizeRejectionThreshold != null) {
            setter.withQueueSizeRejectionThreshold(queueSizeRejectionThreshold);
        }
        Integer rollingStatisticalWindowInMilliseconds = getProperty(classSimpleName, methodName, ROLLING_STATISTICAL_WINDOW_IN_MILLISECONDS, Integer.class);
        if (rollingStatisticalWindowInMilliseconds != null) {
            setter.withMetricsRollingStatisticalWindowInMilliseconds(rollingStatisticalWindowInMilliseconds);
        }
        Integer rollingStatisticalWindowBuckets = getProperty(classSimpleName, methodName, ROLLING_STATISTICAL_WINDOW_BUCKETS, Integer.class);
        if (rollingStatisticalWindowBuckets != null) {
            setter.withMetricsRollingStatisticalWindowBuckets(rollingStatisticalWindowBuckets);
        }
        Boolean allowMaximumSizeToDivergeFromCoreSize = getProperty(classSimpleName, methodName, ALLOW_MAXIMUM_SIZE_TO_DIVERGE_FROM_CORE_SIZE, Boolean.class);
        if (allowMaximumSizeToDivergeFromCoreSize != null) {
            setter.withAllowMaximumSizeToDivergeFromCoreSize(allowMaximumSizeToDivergeFromCoreSize);
        }
        return setter;
    }

    public HystrixMethodConfig buildHystrixMethodConfig(String classSimpleName, String methodName) {
        HystrixMethodConfig methodConfig = new HystrixMethodConfig();
        methodConfig.setGroupKey(classSimpleName);
        methodConfig.setCommandKey(methodName);
        methodConfig.setFallbackClass(getProperty(classSimpleName, methodName, FALLBACK_CLASS, String.class));
        return methodConfig;
    }


    private <T> T getProperty(String classSimpleName, String methodName, String name, Class<T> type) {
        Environment env = context.getEnvironment();
        return env.getProperty(String.format(FORMAT, classSimpleName, methodName, name), type);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
