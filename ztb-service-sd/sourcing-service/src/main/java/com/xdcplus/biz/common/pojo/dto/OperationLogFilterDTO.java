package com.xdcplus.biz.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * (OperationLog)表查询入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-10-19 09:59:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class OperationLogFilterDTO extends PageDTO implements Serializable {
    private static final long serialVersionUID = 334162284259252035L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("日志类型")
    private String type;

    @ApiModelProperty("日志标题")
    private String title;

    @ApiModelProperty("请求地址")
    private String remoteAddr;

    @ApiModelProperty("URI")
    private String requestUri;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("提交参数")
    private String params;

    @ApiModelProperty("异常")
    private String exception;

    @ApiModelProperty("用户人")
    private String userName;

    @ApiModelProperty("添加人")
    private String createdUser;

    @ApiModelProperty("修改人")
    private String updatedUser;

    @ApiModelProperty("创建时间")
    private Long createdTime;

    @ApiModelProperty("修改时间")
    private Long updatedTime;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("是否已经逻辑删除（0：未删除 1：已删除）")
    private Integer deleted;

}
