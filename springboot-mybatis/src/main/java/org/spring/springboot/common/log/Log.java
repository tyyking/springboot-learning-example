package org.spring.springboot.common.log;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * @author niejun
 * @date: 2018年9月30日 下午3:36:05
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /** 模块 */
    String title() default "";

    /** 功能 */
    String action() default "";

    /** 渠道 */
    String channel() default "1";//1后台

    /** 是否保存请求的参数 */
    boolean isSaveRequestData() default true;

}
