package com.xdcplus.vendor.websocket;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 竞价消息
 * @author Rong.Jia
 * @date 2021/09/13
 */
@Data
@ApiModel("竞价消息")
public class BiddingMessage implements Serializable {

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额", required = true)
    private String money;

    /**
     * 表单ID
     */
    @ApiModelProperty(value = "表单ID", required = true)
    private Long requestId;

    /**
     * 出价物标识
     */
    @ApiModelProperty(value = "出价物标识", required = true)
    private String offerGoods;

    /**
     * 出价人
     */
    @ApiModelProperty(value = "出价人", required = true)
    private String offerUser;

    /**
     * 是否有效, 0:无效，1：有效
     */
    @ApiModelProperty(value = "是否有效，详见字典", hidden = true)
    private Byte available;

    /**
     * 是否触发阈值，0:否，1：有
     */
    @ApiModelProperty(value = "是否触发阈值，详见字典", hidden = true)
    private Byte triggerThreshold;

    /**
     *  出价记录
     */
    @ApiModelProperty(value = "出价记录")
    private List<BiddingMessage> children;

    /**
     *  描述
     */
    @ApiModelProperty(value = "描述", hidden = true)
    private String description;


}
