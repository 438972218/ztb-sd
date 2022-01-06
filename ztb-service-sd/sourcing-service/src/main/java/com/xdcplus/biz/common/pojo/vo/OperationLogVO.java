package com.xdcplus.biz.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * (OperationLog)表VO类
 *
 * @author Fish.Fei
 * @since 2021-10-19 09:59:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class OperationLogVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = -37340528176823687L;

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

}
