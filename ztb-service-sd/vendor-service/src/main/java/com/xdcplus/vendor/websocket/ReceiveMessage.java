package com.xdcplus.vendor.websocket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接收消息
 *
 * @author Rong.Jia
 * @date 2021/08/17 13:22:54
 */
@Data
@ApiModel("接收消息信息")
public class ReceiveMessage implements Serializable {

    private static final long serialVersionUID = -1082048284593593572L;

    /**
     * 类型，1-> 竞价，2->公告信息
     */
    @ApiModelProperty("类型，1-> 竞价，2->公告信息")
    private Integer type;

    /**
     * 消息
     */
    @ApiModelProperty("消息")
    private Object message;













}
