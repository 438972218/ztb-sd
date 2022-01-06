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
public class BidDetailCollectVO implements Serializable {
    private static final long serialVersionUID = -18221289337744887L;

    @ApiModelProperty("标类型（商业标，资质标，技术标）")
    private String bidType;

    @ApiModelProperty("类型(报价须知、国内报价、国外报价)")
    private String type;

    @ApiModelProperty("标题")
    private String name;

    @ApiModelProperty("说明")
    private String explaination;

    @ApiModelProperty("数量")
    private String quantity;

    @ApiModelProperty("单位")
    private String unit;


}
