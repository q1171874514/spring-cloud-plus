package com.example.controller;

import com.example.service.ConsumerService;
import com.example.utils.Result;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.concurrent.CompletionService;

@RestController
@RequestMapping("/consumer")
@Slf4j
public class ConsumerController {
    @Autowired
    private ConsumerService consumerService;

    @RequestMapping("moneyAdd")
    public Result moneyAdd(HttpServletResponse response,
                           @RequestParam("id") Long id,
                           @RequestParam("money") BigDecimal money) throws TransactionException {
        System.out.println("分布式事务XID::------" + response.getHeader(RootContext.KEY_XID));
        System.out.println("分布式事务方式::---------" + response.getHeader(RootContext.KEY_BRANCH_TYPE));
        consumerService.moneyAdd(id, money);
        return new Result().ok("增加成功");
    }

    @RequestMapping("moneySubtract")
    public Result moneySubtract(HttpServletResponse response,
                                @RequestParam("id") Long id,
                                @RequestParam("money") BigDecimal money) throws TransactionException {
        System.out.println("分布式事务XID::------" + response.getHeader(RootContext.KEY_XID));
        System.out.println("分布式事务方式::---------" + response.getHeader(RootContext.KEY_BRANCH_TYPE));
        consumerService.moneySubtract(id, money);
        return new Result().ok("增加成功");
    }
}
