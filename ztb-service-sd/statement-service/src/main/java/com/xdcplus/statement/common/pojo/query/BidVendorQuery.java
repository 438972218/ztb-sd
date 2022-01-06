package com.xdcplus.statement.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 招标投标方(BidVendor)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-10-15 13:27:58
 */
@Data
@SuppressWarnings("serial")
public class BidVendorQuery implements Serializable {
    private static final long serialVersionUID = 711688377658531239L;

    private Long id;

    private Long bidSheetId;

    private Long vendorId;

    private Long vendorUserId;

    private String vendorCode;

    private String vendorName;

    private String vendorStatus;

    private String vendorUserName;

    private String email;

    private String againStatus;

    private Long replyTime;

    private Long checkTime;

    private Integer round;

    private Integer qualificationAttQuantity;

    private Integer technologyAttQuantity;

    private Integer qualificationView;

    private Integer technologyView;

    private Integer commerceView;

    private Double totalPrice;

    private Double qualificationScore;

    private Double technologyScore;

    private Double commerceScore;

    private Double totalScore;

    private Double confirmedPrice;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

}
