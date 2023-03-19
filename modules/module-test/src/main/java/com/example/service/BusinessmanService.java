package com.example.service;

import com.example.entity.BusinessmanEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Map;

public interface BusinessmanService extends BaseService<BusinessmanEntity>{
    int moneyAdd(@Param("id") Long[] ids, @Param("money") BigDecimal money);

    int moneySubtract(@Param("id") Long[] ids, @Param("money")BigDecimal money);

    Boolean moneyAddBoolAll(Long[] ids, BigDecimal money);

    Boolean moneySubtractBoolAll(Long[] ids, BigDecimal money);

}
