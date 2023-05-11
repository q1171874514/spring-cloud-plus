package com.example.interceptor;

import com.example.annotation.Idempotent;
import com.example.utils.HttpContextUtils;
import com.example.utils.Result;
import com.google.gson.Gson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class IdempotentInterceptor implements HandlerInterceptor {
    @Autowired
    RedissonClient redissonClient;
    /**
     * 处理方法前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Idempotent annotation = handlerMethod.getMethod().getAnnotation(Idempotent.class);
        if(annotation != null && !getIdempotentLoke(annotation.value())) {
            response.setContentType("application/json;charset=utf-8");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Origin", HttpContextUtils.getOrigin());
            Result error = annotation.msg() == ""?
                    new Result().error(annotation.code()):
                    new Result().error(annotation.code(),annotation.msg());
            String json = new Gson().toJson(error);
            response.getWriter().print(json);
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Idempotent annotation = handlerMethod.getMethod().getAnnotation(Idempotent.class);
        if(annotation != null)
            unIdempotentLoke(annotation.value());
    }

    private boolean getIdempotentLoke(String var) {
        System.out.println("获取重入锁");
        RLock lock = redissonClient.getLock(var);
        return lock.tryLock();
    }

    private void unIdempotentLoke(String var) {
        System.out.println("釋放重入锁");
        RLock lock = redissonClient.getLock(var);
        lock.unlock();
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//
//    }

}
