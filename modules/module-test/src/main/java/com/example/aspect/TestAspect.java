package com.example.aspect;

import com.example.comment.TestAnnotation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TestAspect {
    @Pointcut("@annotation(com.example.comment.TestAnnotation)")
    private void pointcut() { }

    //前置
    @Before("pointcut() && @annotation(testAnnotation)")
    private void before(TestAnnotation testAnnotation) {
        System.out.println("Before   " + testAnnotation.value());
    }

    //后置
    @After("pointcut() && @annotation(testAnnotation)")
    private void after(TestAnnotation testAnnotation) {
        System.out.println("After   " + testAnnotation.value());
    }

    //环绕通知（之前和之后）
    @Around("pointcut() && @annotation(testAnnotation)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, TestAnnotation testAnnotation) {
        System.out.println("Around");
        try {
            return proceedingJoinPoint.proceed();
        }catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {

        }
    }

    //返回通知
    @AfterReturning("pointcut() && @annotation(testAnnotation)")
    private void afterReturning(TestAnnotation testAnnotation) {
        System.out.println("AfterReturning   " + testAnnotation.value());
    }

    //异常通知
    @AfterThrowing("pointcut() && @annotation(testAnnotation)")
    private void afterThrowing(TestAnnotation testAnnotation) {
        System.out.println("AfterThrowing   " + testAnnotation.value());
    }
}
