package com.example.dao;

import com.example.entity.BusinessmanEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface BusinessmanDao extends BaseDao<BusinessmanEntity> {
    int moneyAdd(@Param("ids") Long[] ids, @Param("money") BigDecimal money);

    int moneySubtract(@Param("ids") Long[] ids, @Param("money") BigDecimal money);
}
