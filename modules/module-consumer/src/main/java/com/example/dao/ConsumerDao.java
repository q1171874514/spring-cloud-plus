package com.example.dao;

import com.example.entity.ConsumerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface ConsumerDao extends BaseDao<ConsumerEntity>{
    int moneyAdd(@Param("id") Long id, @Param("money") BigDecimal money);

    int moneySubtract(@Param("id") Long id, @Param("money") BigDecimal money);
}
