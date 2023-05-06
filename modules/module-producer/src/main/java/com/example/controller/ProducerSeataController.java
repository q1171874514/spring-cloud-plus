package com.example.controller;

import com.example.service.ProducerService;
import com.example.utils.Result;
import io.seata.core.exception.TransactionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 使用seata分布式事务
 */
@RestController
@RequestMapping("/seata")
@Slf4j
public class ProducerSeataController {

    @Autowired
    private ProducerService producerService;

    @RequestMapping("moneyAdd")
    public Result moneyAdd(@RequestParam("id") Long id,
                           @RequestParam("consumerId") Long consumerId,
                           @RequestParam("money") BigDecimal money) throws TransactionException {
        producerService.moneyAdd(id,consumerId, money);
        return new Result().ok("增加成功");
    }

    @RequestMapping("moneySubtract")
    public Result moneySubtract(@RequestParam("id") Long id,
                                @RequestParam("consumerId") Long consumerId,
                                @RequestParam("money") BigDecimal money) {
        producerService.moneySubtract(id, consumerId, money);
        return new Result().ok("扣款成功");
    }
}
