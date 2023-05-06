package com.example.hystrix.concurrency;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


import java.util.concurrent.Callable;

/**
 * Created by YangGuanRong
 * date: 2022/3/8
 */
@Slf4j
@Primary
@Component
public class CustomFeignHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {
    public CustomFeignHystrixConcurrencyStrategy() {
        try {

            HystrixPlugins.getInstance().registerConcurrencyStrategy(this);

        } catch (Exception e) {
            log.error("Failed to register Sleuth Hystrix Concurrency Strategy", e);
        }
    }
    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return new WrappedCallable<>(callable, requestAttributes);
    }
    static class WrappedCallable<T> implements Callable<T> {
        private final Callable<T> target;
        private final RequestAttributes requestAttributes;

        public WrappedCallable(Callable<T> target, RequestAttributes requestAttributes) {
            this.target = target;
            this.requestAttributes = requestAttributes;
        }

        /**
         * feign opens the fuse (hystrix): feign.hystrix.enabled=ture, and uses the default signal isolation level,
         * The HttpServletRequest object is independent of each other in the parent thread and the child thread and is not shared.
         * So the HttpServletRequest data of the parent thread used in the child thread is null,
         * naturally it is impossible to obtain the token information of the request header In a multithreaded environment, call before the request, set the context before the call
         *
         * feign启用了hystrix，并且feign.hystrix.enabled=ture。采用了线程隔离策略。
         * HttpServletRequest 请求在对象在父线程和子线程中相互独立，且不共享
         * 所以父线程的 HttpServletRequest 在子线程中为空，
         * 所以通常 在多线程环境中，在请求调用之前设置上下文
         * @return T
         * @throws Exception Exception
         */
        @Override
        public T call() throws Exception {
            try {
                // Set true to share the parent thread's HttpServletRequest object setting
                RequestContextHolder.setRequestAttributes(requestAttributes, true);
                return target.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        }
    }
}
