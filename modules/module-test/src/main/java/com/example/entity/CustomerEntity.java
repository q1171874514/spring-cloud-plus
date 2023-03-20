package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("customer")
public class CustomerEntity {
    private BigDecimal money;
}
