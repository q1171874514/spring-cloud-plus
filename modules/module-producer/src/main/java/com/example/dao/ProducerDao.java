package com.example.dao;

import com.example.entity.ProducerEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProducerDao extends BaseDao<ProducerEntity>{
    int moneyAdd(@Param("id") Long id, @Param("money") BigDecimal money);

    int moneySubtract(@Param("id") Long id, @Param("money") BigDecimal money);
}
