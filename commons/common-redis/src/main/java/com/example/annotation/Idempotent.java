package com.example.annotation;

import com.example.exception.RenException;

import java.lang.annotation.*;

/**
 * 幂等注解
 *
 * @author Mark sunlightcs@gmail.com
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {
    /**
     * 锁名
     * @return the {@link org.springframework.integration.handler.advice.IdempotentReceiverInterceptor}
     * bean references.
     */
    String value();

    /**
     * 失败返回码
     */
    int code() default 500;

    /**
     * 返回内容
     */
    String msg() default "";

}
