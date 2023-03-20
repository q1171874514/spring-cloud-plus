package com.example.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BusCusMoneyVo {
    private Long busId;
    private Long cusId;
    private BigDecimal money;
}
