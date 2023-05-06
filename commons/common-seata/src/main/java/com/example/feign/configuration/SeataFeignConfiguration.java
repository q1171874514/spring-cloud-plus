package com.example.feign.configuration;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * @author lidai
 * @date 2019/2/27 16:14
 * <p>
 * Feign调用的时候添加请求头Token
 */
public class SeataFeignConfiguration implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("分布式事务XID------------------" + RootContext.getXID());
        System.out.println("分布式事务方式------------------" + RootContext.getBranchType());
        addRequestTemplate(requestTemplate, RootContext.KEY_XID, RootContext.getXID());
//        addRequestTemplate(requestTemplate, RootContext.KEY_BRANCH_TYPE, RootContext.getBranchType());



    }

    private void addRequestTemplate(RequestTemplate requestTemplate, String key, String value) {
        if (StringUtils.isNotEmpty(value)) {
            requestTemplate.header(key, value);
        }
    }
}
