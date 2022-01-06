package com.xdcplus.vendor.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 出价记录VO
 *
 * @author Rong.Jia
 * @date 2021/08/17 09:46:09
 */
@ApiModel("出价记录信息 对照对象")
@EqualsAndHashCode(callSuper = true)
@Data
public class OfferVO extends BaseBO implements Serializable {

    private static final long serialVersionUID = 4562076174607707963L;

    /**
     * 金额
     */
    @ApiModelProperty("金额")
    private String money;

    /**
     * 出价时间
     */
    @ApiModelProperty("出价时间")
    private Long offerTime;

    /**
     * 父级ID
     */
    @ApiModelProperty("父级ID")
    private Long parentId;

    /**
     * 出价人
     */
    @ApiModelProperty("出价人")
    private String offerUser;

    /**
     * 表单ID
     */
    @ApiModelProperty("表单ID")
    private Long requestId;

    /**
     * 排名
     */
    @ApiModelProperty("排名")
    private Integer ranking;

    /**
     * 出价物标识
     */
    @ApiModelProperty("出价物标识")
    private String offerGoods;

    /**
     * 供应商ID
     */
    @ApiModelProperty("供应商ID")
    private Long vendorId;

    /**
     * 供应商标识
     */
    @ApiModelProperty("供应商标识")
    private String vendorMark;

    /**
     * 供应商名称
     */
    @ApiModelProperty("供应商名称")
    private String vendorName;

    /**
     * 是否有效, 0:无效，1：有效
     */
    @ApiModelProperty(value = "是否有效，详见字典")
    private Byte available;

    /**
     * 是否触发阈值，0:否，1：有
     */
    @ApiModelProperty(value = "是否触发阈值，详见字典")
    private Byte triggerThreshold;

    /**
     *  出价记录
     */
    @ApiModelProperty(value = "出价记录")
    private List<OfferVO> children;







}
