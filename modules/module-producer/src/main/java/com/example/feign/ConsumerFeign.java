package com.example.feign;


import com.example.feign.configuration.SeataFeignConfiguration;
import com.example.feign.factory.ConsumerFallbackFactory;
import com.example.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

// 填入注册中心中的应用名, 也就是要调用的微服务的应用名
@FeignClient(name = "module-consumer",
        configuration = {SeataFeignConfiguration.class},
        path = "/",
        fallbackFactory = ConsumerFallbackFactory.class)
@RequestMapping("/consumer")
public interface ConsumerFeign {

    @RequestMapping("moneyAdd")
    Result moneyAdd(@RequestParam("id") Long id, @RequestParam("money") BigDecimal money);

    @RequestMapping("moneySubtract")
    Result moneySubtract(@RequestParam("id") Long id, @RequestParam("money") BigDecimal money);

}