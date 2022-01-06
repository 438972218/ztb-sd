package com.xdcplus.statement.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * (OperationLog)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-10-19 09:59:04
 */
@Data
@SuppressWarnings("serial")
public class OperationLogQuery implements Serializable {
    private static final long serialVersionUID = 284164630502486240L;

    private Long id;

    private String type;

    private String title;

    private String remoteAddr;

    private String requestUri;

    private String method;

    private String params;

    private String exception;

    private String userName;

    private String createdUser;

    private String updatedUser;

    private Long createdTime;

    private Long updatedTime;

    private String description;

    private Integer deleted;

}
