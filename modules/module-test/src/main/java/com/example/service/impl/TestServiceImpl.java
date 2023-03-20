package com.example.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.dao.TestDao;
import com.example.dto.TestDTO;
import com.example.entity.TestEntity;
import com.example.exception.RenException;
import com.example.service.BusinessmanService;
import com.example.service.CustomerService;
import com.example.service.TestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class TestServiceImpl extends CrudServiceImpl<TestDao, TestEntity, TestDTO> implements TestService {
    @Autowired
    private BusinessmanService businessmanService;
    @Autowired
    private CustomerService customerService;

    @Override
    public QueryWrapper<TestEntity> getWrapper(Map<String, Object> params) {
        String name = (String) params.get("name");
        QueryWrapper<TestEntity> wrapper = new QueryWrapper<>();
//        wrapper.like(StringUtils.isEmpty(name), "name", name);
//        String s = wrapper.toString();
        return wrapper;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void busToCusMoney(Long busId, Long cusId, BigDecimal money) {
        if (!businessmanService.moneySubtractBoolAll(new Long[]{busId}, money)
                || !customerService.moneyAddBoolAll(new Long[]{cusId}, money))
            throw new RenException("交易失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void cusToBusMoney(Long busId, Long cusId, BigDecimal money) {
        if (!customerService.moneySubtractBoolAll(new Long[]{busId}, money)
                || !businessmanService.moneyAddBoolAll(new Long[]{cusId}, money))
            throw new RenException("交易失败");
    }
}
