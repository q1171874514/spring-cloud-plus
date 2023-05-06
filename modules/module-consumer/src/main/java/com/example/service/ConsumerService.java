package com.example.service;

import com.example.entity.ConsumerEntity;
import io.seata.core.exception.TransactionException;

import java.math.BigDecimal;


public interface ConsumerService extends BaseService<ConsumerEntity>{
    Boolean moneyAdd(Long id, BigDecimal money) throws TransactionException;
    Boolean moneySubtract(Long id, BigDecimal money) throws TransactionException;
}
