package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dao.CustomerDao;
import com.example.entity.BusinessmanEntity;
import com.example.entity.CustomerEntity;
import com.example.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerServiceImpl extends BaseServiceImpl<CustomerDao, CustomerEntity> implements CustomerService {

    @Override
    public int moneyAdd(Long[] ids, BigDecimal money) {
        return baseDao.moneyAdd(ids, money);
    }

    @Override
    public int moneySubtract(Long[] ids, BigDecimal money) {
        return baseDao.moneySubtract(ids, money);
    }

    @Override
    public Boolean moneyAddBoolAll(Long[] ids, BigDecimal money) {
        QueryWrapper<CustomerEntity> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        Integer count = baseDao.selectCount(wrapper);
        return !count.equals(0) && count.equals(this.moneyAdd(ids, money));
    }

    @Override
    public Boolean moneySubtractBoolAll(Long[] ids, BigDecimal money) {
        QueryWrapper<CustomerEntity> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        Integer count = baseDao.selectCount(wrapper);
        return !count.equals(0) && count.equals(this.moneySubtract(ids, money));
    }
}
