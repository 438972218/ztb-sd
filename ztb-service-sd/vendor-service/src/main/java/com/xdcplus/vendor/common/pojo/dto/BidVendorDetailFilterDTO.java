package com.xdcplus.vendor.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商内容明细（国内报价、国外报价）(BidVendorDetail)表查询入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-08-27 11:28:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidVendorDetailFilterDTO extends PageDTO implements Serializable {
    private static final long serialVersionUID = -18307294532284199L;

    @ApiModelProperty("信息主键")
    private Long id;

    @ApiModelProperty("招标单id")
    private Long bidSheetId;

    @ApiModelProperty("招标供应商id")
    private Long bidVendorId;

    @ApiModelProperty("标类型（商业标，资质标，技术标）")
    private String bidType;

    @ApiModelProperty("类型(报价须知、国内报价、国外报价)")
    private String type;

    @ApiModelProperty("明细状态（保存/提交/历史）")
    private String status;

    @ApiModelProperty("轮次")
    private Integer round;

    @ApiModelProperty("标题")
    private String name;

    @ApiModelProperty("说明")
    private String explaination;

    @ApiModelProperty("数量")
    private Double quantity;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("var1")
    private String var1;

    @ApiModelProperty("var2")
    private String var2;

    @ApiModelProperty("var3")
    private String var3;

    @ApiModelProperty("var4")
    private String var4;

    @ApiModelProperty("var5")
    private String var5;

    @ApiModelProperty("单价")
    private Double price;

    @ApiModelProperty("总价")
    private Double totalPrice;

    @ApiModelProperty("ETD")
    private String etd;

    @ApiModelProperty("ETA")
    private String eta;

    @ApiModelProperty("船期（每周几）")
    private String shippingSchedule;

    @ApiModelProperty("船公司")
    private String shippingCompany;

    @ApiModelProperty("创建人")
    private String createdUser;

    @ApiModelProperty("创建时间")
    private Long createdTime;

    @ApiModelProperty("修改人")
    private String updatedUser;

    @ApiModelProperty("修改时间")
    private Long updatedTime;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("是否已经逻辑删除（0：未删除 1：已删除）")
    private Integer deleted;

    @ApiModelProperty("明细状态List（保存/提交/历史）")
    private List<String> statusList;

}
