package com.xdcplus.biz.common.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商内容明细（国内报价、国外报价）(BidVendorDetail)表VO类
 *
 * @author Fish.Fei
 * @since 2021-08-27 11:27:30
 */
@Data
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidVendorDetailTypeVO implements Serializable {
    private static final long serialVersionUID = -18221289337744866L;

    @ApiModelProperty("类型(报价须知、国内报价、国外报价)")
    private String type;

    @ApiModelProperty("供应商内容明细")
    private List<BidVendorDetailVO> bidVendorDetailVOS;
}
