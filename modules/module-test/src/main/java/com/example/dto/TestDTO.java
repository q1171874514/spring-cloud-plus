package com.example.dto;

import com.example.validator.group.AddGroup;
import com.example.validator.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "用户管理")
public class TestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Null(message = "{id.null}", groups = AddGroup.class)
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
    private Long id;

    private String name;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createDate;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updateDate;

    @ApiModelProperty(value = "创建者")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long creator;

    @ApiModelProperty(value = "更新者")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long updater;

}
