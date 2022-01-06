package com.xdcplus.vendor.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 竞价品项(PaidMaterial)表VO类
 *
 * @author Fish.Fei
 * @since 2021-09-03 14:17:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class PaidMaterialVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = -69737975346469293L;

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

    @ApiModelProperty("到达触发价格后最小出价幅度")
    private Double triggerMinimumBidRange;

    @ApiModelProperty("子品项")
    private List<PaidMaterialVO> childPaidMaterialVOS;

    @ApiModelProperty("出价记录")
    private List<OfferVO> offerVOS;

    @ApiModelProperty("授权信息")
    private PaidMaterialMandateVO paidMaterialMandateVO;
}
