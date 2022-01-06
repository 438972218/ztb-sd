package com.xdcplus.vendor.common.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidTotalRankingExcelVO implements Serializable {

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private String money;

    /**
     * 出价人
     */
    @ApiModelProperty("出价人")
    private String offerUser;

    /**
     * 排名
     */
    @ApiModelProperty("排名")
    private Integer ranking;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String vendorName;

    /**
     * 出价时间
     */
    @ApiModelProperty("出价时间")
    private String offerTime;
}
