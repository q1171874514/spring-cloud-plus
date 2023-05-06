package com.example.service;

import com.example.entity.CustomerEntity;

import java.math.BigDecimal;

public interface CustomerService extends BaseService<CustomerEntity> {
    int moneyAdd(Long[] ids, BigDecimal money);

    int moneySubtract(Long[] ids, BigDecimal money);

    Boolean moneyAddBoolAll(Long[] ids, BigDecimal money);

    Boolean moneySubtractBoolAll(Long[] ids, BigDecimal money);
}
