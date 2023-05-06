package com.example.service.impl;

import com.example.dao.ConsumerDao;
import com.example.entity.ConsumerEntity;
import com.example.exception.ErrorCode;
import com.example.exception.RenException;
import com.example.service.ConsumerService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.core.model.BranchType;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.cloud.seata.feign.SeataFeignClient;

import java.math.BigDecimal;


@Service
@Slf4j
public class ConsumerServiceImpl extends BaseServiceImpl<ConsumerDao, ConsumerEntity> implements ConsumerService {

    @Override
    @Transactional
    public Boolean moneyAdd(Long id, BigDecimal money) throws TransactionException {
//        RootContext.bindBranchType(BranchType.XA);
        log.info("Seata全局事务id=================>{}", RootContext.getXID());
        log.info("Seata全局事务模式=================>{}", RootContext.getBranchType());
        baseDao.moneyAdd(id, money);
        if(this.selectById(id).getMoney().compareTo(BigDecimal.valueOf(0)) == -1) {
            throw new RenException(ErrorCode.NOT_NULL);
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean moneySubtract(Long id, BigDecimal money) throws TransactionException {
//        RootContext.bindBranchType(BranchType.XA);
        log.info("Seata全局事务id=================>{}", RootContext.getXID());
        log.info("Seata全局事务模式=================>{}", RootContext.getBranchType());
        baseDao.moneySubtract(id, money);
        ConsumerEntity consumerEntity = this.selectById(id);
        if(consumerEntity.getMoney().compareTo(BigDecimal.valueOf(0)) == -1) {
            throw new RenException(ErrorCode.NOT_NULL);
        }
        return true;
    }

}
