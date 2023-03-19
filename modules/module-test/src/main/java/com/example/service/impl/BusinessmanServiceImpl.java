package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dao.BusinessmanDao;
import com.example.entity.BusinessmanEntity;
import com.example.service.BusinessmanService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import sun.security.util.ArrayUtil;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class BusinessmanServiceImpl extends BaseServiceImpl<BusinessmanDao, BusinessmanEntity> implements BusinessmanService {

    @Override
    public int moneyAdd(Long[] ids, BigDecimal money) {
        return baseDao.moneyAdd(ids, money);
    }

    @Override
    public int moneySubtract(Long[] ids, BigDecimal money) {
        return baseDao.moneySubtract(ids,money);
    }

    @Override
    public Boolean moneyAddBoolAll(Long[] ids, BigDecimal money) {
        QueryWrapper<BusinessmanEntity> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        Integer count = baseDao.selectCount(wrapper);
        return !count.equals(0) && count.equals(this.moneyAdd(ids, money));
    }

    @Override
    public Boolean moneySubtractBoolAll(Long[] ids, BigDecimal money) {
        QueryWrapper<BusinessmanEntity> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids);
        Integer count = baseDao.selectCount(wrapper);
        return !count.equals(0) && count.equals(this.moneySubtract(ids, money));
    }



}
