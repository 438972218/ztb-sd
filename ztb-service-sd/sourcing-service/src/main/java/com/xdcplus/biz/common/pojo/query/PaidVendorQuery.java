package com.xdcplus.biz.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 竞价供应商(PaidVendor)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-16 11:57:52
 */
@Data
@SuppressWarnings("serial")
public class PaidVendorQuery implements Serializable {
    private static final long serialVersionUID = -85011684556184454L;

    private Long id;

    private Long paidSheetId;

    private Long vendorId;

    private Long vendorUserId;

    private String vendorName;

    private String vendorStatus;

    private String vendorMark;

    private String city;

    private String linkman;

    private String phone;

    private String email;

    private Double startPrice;

    private Double totalPrice;

    private Double confirmedPrice;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

}
