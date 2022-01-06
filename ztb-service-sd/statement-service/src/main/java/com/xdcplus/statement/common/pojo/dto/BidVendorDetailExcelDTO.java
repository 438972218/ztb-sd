package com.xdcplus.statement.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商内容明细（国内报价、国外报价）(BidVendorDetail)表更新入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-08-27 11:27:30
 */
@Data
@EqualsAndHashCode()
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidVendorDetailExcelDTO implements Serializable {
    private static final long serialVersionUID = 238853875149674399L;

    @ApiModelProperty("标类型（商业标，资质标，技术标）")
    private String bidType;

    @ApiModelProperty("类型(报价须知、国内报价、国外报价)")
    private String type;

    @ApiModelProperty("标题")
    private String name;

    @ApiModelProperty("说明")
    private String explaination;

    @ApiModelProperty("数量")
    private Double quantity;

    @ApiModelProperty("单位")
    private String unit;

    private List<BidVendorDetailExcelVendorDTO> bidVendorDetailExcelVendorDTOS;

    @Data
    public static class BidVendorDetailExcelVendorDTO implements Serializable{
        private static final long serialVersionUID = 238853875149674349L;

        @ApiModelProperty("招标供应商id")
        private Long bidVendorId;

        @ApiModelProperty("招标供应商名称")
        private String bidVendorName;

        @ApiModelProperty("轮次")
        private Integer round;

        @ApiModelProperty("单价")
        private Double price;

        @ApiModelProperty("ETD")
        private String etd;

        @ApiModelProperty("ETA")
        private String eta;

        @ApiModelProperty("船期（每周几）")
        private String shippingSchedule;

        @ApiModelProperty("船公司")
        private String shippingCompany;

    }

}
