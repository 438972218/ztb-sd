package com.xdcplus.workflow.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 流转信息 流转节点DTO
 *
 * @author Rong.Jia
 * @date 2021/10/09
 */
@Data
@ApiModel("流转节点 过滤查询 对照对象")
public class RequestFlowTransferNodeFilterDTO implements Serializable {

    /**
     *  表单ID
     */
    @NotNull(message = "表单ID 不能为空")
    @ApiModelProperty(value = "表单ID", required = true)
    private Long requestId;

    /**
     * 去向状态ID
     */
    @ApiModelProperty("去向状态ID")
    private Long toStatusId;

    /**
     *  上一个状态ID
     */
    @ApiModelProperty("上一个状态ID")
    private Long fromStatusId;


}
