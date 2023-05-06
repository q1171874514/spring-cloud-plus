package com.example.service.impl;

import com.example.dao.ProducerDao;
import com.example.entity.ProducerEntity;
import com.example.exception.ErrorCode;
import com.example.exception.RenException;
import com.example.feign.ConsumerFeign;
import com.example.service.ProducerService;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.core.model.BranchType;
import io.seata.spring.annotation.GlobalTransactional;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.cloud.seata.feign.SeataFeignClient;
import java.math.BigDecimal;
import io.seata.tm.api.TransactionalTemplate;

@Service
@Slf4j
public class ProducerServiceImpl extends BaseServiceImpl<ProducerDao, ProducerEntity> implements ProducerService {
    @Autowired
    private ConsumerFeign consumerFeign;

    @Override
    @GlobalTransactional
    public Boolean moneyAdd(Long id, Long consumerId, BigDecimal money) {
        //设置XA模式，（不设置是默认TA模式）
        RootContext.bindBranchType(BranchType.XA);
        log.info("Seata全局事务id=================>{}", RootContext.getXID());
        log.info("Seata全局事务模式=================>{}", RootContext.getBranchType());
        boolean producerAdd = (baseDao.moneyAdd(id, money) == 1);
        boolean consumerSuccess = consumerFeign.moneySubtract(consumerId, money).success();
        if(!consumerSuccess || !producerAdd) {
            throw new RenException(ErrorCode.NOT_NULL);
        }
        return true;
    }

    @Override
    @GlobalTransactional
    public Boolean moneySubtract(Long id, Long consumerId, BigDecimal money) {
        log.info("Seata全局事务id=================>{}", RootContext.getXID());
        log.info("Seata全局事务模式=================>{}", RootContext.getBranchType());
        boolean consumerSuccess = consumerFeign.moneyAdd(consumerId, money).success();
        boolean producerSuccess = (baseDao.moneySubtract(id, money) == 1);
        if(!consumerSuccess || !producerSuccess) {
            throw new RenException(ErrorCode.FORBIDDEN);
        }
        return true;
    }
}
