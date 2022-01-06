package com.xdcplus.vendor.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 出价信息DTO
 *
 * @author Rong.Jia
 * @date 2021/08/17 10:22:06
 */
@Data
@ApiModel("出价参数对照对象")
public class OfferDTO implements Serializable {

    private static final long serialVersionUID = -5237204252934995624L;

    /**
     * 金额
     */
    @NotBlank(message = "金额 不能为空")
    @ApiModelProperty(value = "金额", required = true)
    private String money;

    /**
     * 表单ID
     */
    @NotNull(message = "表单ID 不能为空")
    @ApiModelProperty(value = "表单ID", required = true)
    private Long requestId;

    /**
     * 出价物标识
     */
    @NotBlank(message = "出价物标识 不能为空")
    @ApiModelProperty(value = "出价物标识", required = true)
    private String offerGoods;

    /**
     * 出价人
     */
    @NotBlank(message = "出价人 不能为空")
    @ApiModelProperty(value = "出价人", required = true)
    private String offerUser;

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
    private List<OfferDTO> children;

    /**
     *  描述
     */
    @ApiModelProperty(value = "描述")
    private String description;















}
