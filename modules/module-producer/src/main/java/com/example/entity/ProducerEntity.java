package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("producer")
public class ProducerEntity extends BaseEntity{

    private String name;
    private Integer number;
}
