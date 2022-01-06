package com.xdcplus.statement.common.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商内容明细（国内报价、国外报价）表VO类
 *
 * @author Fish.Fei
 * @since 2021-08-27 11:27:30
 */
@Data
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidVendorDetailCollectVO implements Serializable {
    private static final long serialVersionUID = -18221289337744867L;

    @ApiModelProperty("招标供应商ID")
    private Long bidVendorId;

    @ApiModelProperty("轮次")
    private String round;

    @ApiModelProperty("单价")
    private String price;

    @ApiModelProperty("ETD")
    private String etd;

    @ApiModelProperty("ETA")
    private String eta;

    @ApiModelProperty("船期（每周几）")
    private String shippingSchedule;

    @ApiModelProperty("船公司")
    private String shippingCompany;

}
