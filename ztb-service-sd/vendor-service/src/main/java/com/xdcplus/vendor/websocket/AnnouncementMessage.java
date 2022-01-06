package com.xdcplus.vendor.websocket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 公告消息
 *
 * @author Rong.Jia
 * @date 2021/09/13
 */
@Data
@ApiModel("公告消息")
public class AnnouncementMessage implements Serializable {

    /**
     * 公告类型(1->全局公告，2->表单公告)
     */
    @ApiModelProperty(value = "公告类型(1->全局公告，2->表单公告)", required = true)
    private Byte type;

    /**
     * 表单ID(全局公告->-1)
     */
    @ApiModelProperty(value = "表单ID(全局公告->-1)",required = true)
    private Long requestId;

    /**
     * 公告内容
     */
    @ApiModelProperty(value = "公告内容",required = true)
    private String message;

}
