package com.example.dto;

import com.example.entity.BaseEntity;
import com.example.validator.group.AddGroup;
import com.example.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
@Data
public class ConsumerDTO {
    @Null(message="{id.null}", groups = AddGroup.class)
    @NotNull(message="{id.require}", groups = UpdateGroup.class)
    private Long id;
    private String name;
    private Integer number;
    private BigDecimal money;
}
