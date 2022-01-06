package com.xdcplus.biz.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:45:31
 */
@Data
@SuppressWarnings("serial")
public class PaidMaterialMandateQuery implements Serializable {
    private static final long serialVersionUID = -90825326694261092L;

    private Long id;

    private Long paidMaterialId;

    private Long paidVendorId;

    private Long paidVendorUserId;

    private Double price;

    private Double confirmedPrice;

    private String remark;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

}
