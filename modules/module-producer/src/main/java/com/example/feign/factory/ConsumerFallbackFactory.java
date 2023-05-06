package com.example.feign.factory;

import com.example.feign.ConsumerFeign;
import com.example.utils.Result;
import org.springframework.stereotype.Service;
import feign.hystrix.FallbackFactory;

import java.math.BigDecimal;

@Service
public class ConsumerFallbackFactory implements FallbackFactory<ConsumerFeign>{
    @Override
    public ConsumerFeign create(Throwable throwable) {
        return new ConsumerFeign() {
            @Override
            public Result moneyAdd(Long id, BigDecimal money) {
                return new Result().error("Consumer：moneyAdd：连接时报");
            }

            @Override
            public Result moneySubtract(Long id, BigDecimal money) {
                return new Result().error("Consumer：moneySubtract：连接时报");
            }
        };
    }
}
