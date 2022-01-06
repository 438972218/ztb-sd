package com.xdcplus.biz.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class BidVendorDetailMapVO implements Serializable {
    private static final long serialVersionUID = -18221289337744863L;

    @ApiModelProperty("轮次")
    private Integer round;

    @ApiModelProperty("供应商内容明细")
    private List<BidVendorDetailTypeVO> bidVendorDetailTypeVOS;
}
