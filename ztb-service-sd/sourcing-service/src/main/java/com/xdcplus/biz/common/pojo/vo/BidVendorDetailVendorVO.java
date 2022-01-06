package com.xdcplus.biz.common.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商内容明细（国内报价、国外报价）表VO类
 *
 * @author Fish.Fei
 * @since 2021-08-27 11:27:30
 */
@Data
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidVendorDetailVendorVO implements Serializable {
    private static final long serialVersionUID = -18221289337743867L;

    @ApiModelProperty("招标供应商ID")
    private Long bidVendorId;

    private List<BidVendorDetailVO> bidVendorDetailVOS;

}
