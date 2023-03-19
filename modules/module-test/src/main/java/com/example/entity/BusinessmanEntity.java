package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("businessman")
public class BusinessmanEntity extends BaseEntity{
    private BigDecimal money;
}
