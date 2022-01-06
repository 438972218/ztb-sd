package com.xdcplus.ztb.common.remote.domain.sourcing;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode()
public class BidRanking implements Serializable {

    private static final long serialVersionUID = 4688332847574525807L;
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
     * 出价物标识
     */
    @ApiModelProperty("出价物标识")
    private String offerGoods;

    /**
     * 供应商ID
     */
    @ApiModelProperty("供应商ID")
    private Long vendorId;

    /**
     * 供应商标识
     */
    @ApiModelProperty("供应商标识")
    private String vendorMark;

    /**
     * 出价时间
     */
    @ApiModelProperty("出价时间")
    private Long offerTime;

}
