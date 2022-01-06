package com.xdcplus.vendor.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表VO类
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:49:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class PaidMaterialMandateVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = 910746856200767403L;

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

}
