package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("consumer")
public class ConsumerEntity extends BaseEntity{
    private String name;
    private Integer number;
    private BigDecimal money;
}
