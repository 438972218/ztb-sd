package com.xdcplus.vendor.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表查询入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:49:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class PaidMaterialMandateFilterDTO extends PageDTO implements Serializable {
    private static final long serialVersionUID = 669011426755883946L;

    @ApiModelProperty("信息主键")
    private Long id;

    @ApiModelProperty("竞价品项id")
    private Long paidMaterialId;

    @ApiModelProperty("竞价供应商id")
    private Long paidVendorId;

    @ApiModelProperty("竞价供应商用户id")
    private Long paidVendorUserId;

    @ApiModelProperty("供应商出价")
    private Double price;

    @ApiModelProperty("确认价格")
    private Double confirmedPrice;

    @ApiModelProperty("评论")
    private String remark;

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

}
