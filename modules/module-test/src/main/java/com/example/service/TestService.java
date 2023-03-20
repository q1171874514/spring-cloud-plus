package com.example.service;

import com.example.dto.TestDTO;
import com.example.entity.TestEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface TestService extends CrudService<TestEntity, TestDTO> {

    void busToCusMoney(Long busId, Long cusId, BigDecimal money);

    void cusToBusMoney(Long cusId, Long busId, BigDecimal money);
}
