package com.xdcplus.statement.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 竞价品项(PaidMaterial)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-06 14:55:26
 */
@Data
@SuppressWarnings("serial")
public class PaidMaterialQuery implements Serializable {
    private static final long serialVersionUID = -78738281122752189L;

    private Long id;

    private Long paidSheetId;

    private Long parentId;

    private Integer materialMark;

    private String materialName;

    private String remark;

    private Long beginTime;

    private Long endTime;

    private String unit;

    private Double quantity;

    private Double startPrice;

    private String minimumBidRule;

    private Double minimumBidRange;

    private Double triggerPrice;

    private String triggerStatus;

    private Double triggerMinimumBidRange;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

}
