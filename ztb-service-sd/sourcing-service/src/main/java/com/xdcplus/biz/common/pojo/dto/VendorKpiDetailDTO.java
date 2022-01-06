package com.xdcplus.biz.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商绩效考核表明细(VendorKpiDetail)表更新入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-09-28 17:37:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class VendorKpiDetailDTO extends BaseBO implements Serializable {
    private static final long serialVersionUID = 136768026777604531L;

    @ApiModelProperty("信息主键")
    private Long id;

    @ApiModelProperty("绩效考核表ID")
    private Long vendorKpiId;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("考核指标")
    private String type;

    @ApiModelProperty("考核子指标")
    private String name;

    @ApiModelProperty("考察情况")
    private String content;

    @ApiModelProperty("权重")
    private String balance;

    @ApiModelProperty("得分范围")
    private String scoreScope;

    @ApiModelProperty("评分")
    private String score;

    @ApiModelProperty("考察描述")
    private String insDesc;

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

    @ApiModelProperty("子评分项")
    private List<VendorKpiDetailDTO> childVendorKpiDetailDTOS;

}
