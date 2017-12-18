package com.dubbo.hystrix;

import lombok.Data;

/**
 * hystrix 方法配置
 *
 * @author liujiawei
 * @create 2017-12-18 下午1:52
 */
@Data
public class HystrixMethodConfig {
    private String groupKey;

    private String commandKey;

    private String fallbackClass;

}
