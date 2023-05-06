package com.example.vo;

import com.example.entity.BaseEntity;
import lombok.Data;

@Data
public class ProducerVo extends BaseEntity {
    private String name;
    private Integer number;
}
