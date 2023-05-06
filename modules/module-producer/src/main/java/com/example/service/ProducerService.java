package com.example.service;

import com.example.entity.ProducerEntity;
import io.seata.core.exception.TransactionException;

import java.math.BigDecimal;

public interface ProducerService extends BaseService<ProducerEntity>{
    Boolean moneyAdd(Long id, Long consumerId, BigDecimal money) throws TransactionException;
    Boolean moneySubtract(Long id, Long consumerId,BigDecimal money);
}
