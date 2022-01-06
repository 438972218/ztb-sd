package com.xdcplus.vendor.websocket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 状态消息
 *
 * @author Rong.Jia
 * @date 2021/08/17 16:57:30
 */
@Data
@ApiModel("状态消息")
public class StateMessage {

    /**
     * 供应商ID
     */
    @ApiModelProperty("供应商ID")
    private Long vendorId;

    /**
     *  表单ID
     */
    @ApiModelProperty("表单ID")
    private Long requestId;

    /**
     * 状态，在线：1，0：离线
     */
    @ApiModelProperty("状态，在线：1，0：离线")
    private Integer state;

    /**
     * 竞价状态，1：进行中，2：暂停，3：已终止，4：截止, -1:未知
     */
    @ApiModelProperty("竞价状态，1：进行中，2：暂停，3：已终止，4：截止, -1:未知")
    private Integer paidStatus;

    /**
     * 供应商标识
     */
    @ApiModelProperty("供应商标识")
    private String vendorMark;


}
