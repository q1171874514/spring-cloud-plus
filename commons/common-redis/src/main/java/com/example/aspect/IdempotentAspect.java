package com.example.aspect;

import com.example.annotation.Idempotent;
import com.example.exception.RenException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
//@Aspect
public class IdempotentAspect {
//    @Autowired
//    RedissonClient redissonClient;
//
//    @Pointcut("@annotation(com.example.annotation.Idempotent)")
//    private void pointcut() {
//    }
//
//    //前置
//    @Before("pointcut() && @annotation(idempotent)")
//    private void before(Idempotent idempotent) {
//        String value = idempotent.value();
//        if(!getIdempotentLoke(value)) {
//            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//            HttpServletResponse response = attributes.getResponse();
//        }
//
//    }
//
//    private boolean getIdempotentLoke(String var) {
//        System.out.println("获取重入锁");
//        RLock lock = redissonClient.getLock("submit");
//        return lock.tryLock();
//    }
//
//    //后置
//    @After("pointcut() && @annotation(idempotent)")
//    private void after(Idempotent idempotent) {
//        System.out.println("After   " + idempotent.value());
//    }
//
//    //环绕通知（之前和之后）
//    @Around("pointcut() && @annotation(idempotent)")
//    public Object around(ProceedingJoinPoint proceedingJoinPoint, Idempotent idempotent) {
//        System.out.println("Around");
//        try {
//            return proceedingJoinPoint.proceed();
//        } catch (Throwable e) {
//            throw new RuntimeException(e);
//        } finally {
//
//        }
//    }
//
//    //返回通知
//    @AfterReturning("pointcut() && @annotation(idempotent)")
//    private void afterReturning(Idempotent idempotent) {
//        System.out.println("AfterReturning   " + idempotent.value());
//    }
//
//    //异常通知
//    @AfterThrowing("pointcut() && @annotation(idempotent)")
//    private void afterThrowing(Idempotent idempotent) {
//        System.out.println("AfterThrowing   " + idempotent.value());
//    }
}
