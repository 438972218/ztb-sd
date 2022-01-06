package com.xdcplus.statement.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 供应商绩效考核表明细(VendorKpiDetail)表VO类
 *
 * @author Fish.Fei
 * @since 2021-09-28 17:37:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class VendorKpiDetailVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = -55376331494535822L;

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


    @ApiModelProperty("子评分项")
    private List<VendorKpiDetailVO> childVendorKpiDetailVOS;

}
