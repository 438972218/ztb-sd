package com.xdcplus.statement.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 招标单内容明细（报价须知、国内报价、国外报价）(BidDetail)表更新入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-08-27 11:26:48
 */
@Data
@EqualsAndHashCode()
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidDetailUpdateDTO implements Serializable {
    private static final long serialVersionUID = -76486179613164437L;

    @ApiModelProperty("招标单id")
    private Long bidSheetId;

    @ApiModelProperty("标类型（商业标，资质标，技术标）")
    private String bidType;

    @ApiModelProperty("信息主键")
    private List<BidDetailDTO> bidDetailDTOS;

}
