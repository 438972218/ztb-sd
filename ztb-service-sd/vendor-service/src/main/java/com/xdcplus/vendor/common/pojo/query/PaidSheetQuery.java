package com.xdcplus.vendor.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 竞价单(PaidSheet)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-07 10:33:29
 */
@Data
@SuppressWarnings("serial")
public class PaidSheetQuery implements Serializable {
    private static final long serialVersionUID = -97683025411458747L;

    private Long id;

    private Long requestId;

    private Long projectSheetId;

    private Integer paidStatusMark;

    private String paidStatus;

    private String paidNum;

    private String title;

    private Long offerStartTime;

    private Long offerEndTime;

    private Double budget;

    private String currency;

    private Integer whetherTest;

    private String paidDirection;

    private String bidStructure;

    private String vendorMonitorRules;

    private String startPriceRule;

    private Integer delayInterval;

    private Long pauseTime;

    private String items;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;




    private List<Long> requestIds;

    private String keyword;

    private Long vendorUserId;

    private Long statusId;

}
