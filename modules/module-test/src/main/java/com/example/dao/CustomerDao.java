package com.example.dao;

import com.example.entity.CustomerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface CustomerDao extends BaseDao<CustomerEntity>{

    int moneyAdd(@Param("ids") Long[] ids, @Param("money")BigDecimal money);

    int moneySubtract(@Param("ids") Long[] ids, @Param("money")BigDecimal money);
}
