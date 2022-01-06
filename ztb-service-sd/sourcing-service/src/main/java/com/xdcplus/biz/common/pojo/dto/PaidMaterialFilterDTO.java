package com.xdcplus.biz.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.dto.PageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 竞价品项(PaidMaterial)表查询入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-09-06 14:55:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class PaidMaterialFilterDTO extends PageDTO implements Serializable {
    private static final long serialVersionUID = -61212551121719313L;

    @ApiModelProperty("信息主键")
    private Long id;

    @ApiModelProperty("竞价单id")
    private Long paidSheetId;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("物品标识")
    private Integer materialMark;

    @ApiModelProperty("物品名称")
    private String materialName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("竞价开始时间")
    private Long beginTime;

    @ApiModelProperty("竞价截止时间")
    private Long endTime;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("数量")
    private Double quantity;

    @ApiModelProperty("起始价格")
    private Double startPrice;

    @ApiModelProperty("最小出价规则")
    private String minimumBidRule;

    @ApiModelProperty("最小出价幅度")
    private Double minimumBidRange;

    @ApiModelProperty("触发价格")
    private Double triggerPrice;

    @ApiModelProperty("触发状态")
    private String triggerStatus;

    @ApiModelProperty("到达触发价格后最小出价幅度")
    private Double triggerMinimumBidRange;

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
