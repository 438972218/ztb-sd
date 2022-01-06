package com.xdcplus.biz.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * bid招标单(BidSheet)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-17 17:38:15
 */
@Data
@SuppressWarnings("serial")
public class BidSheetQuery implements Serializable {
    private static final long serialVersionUID = -39643853055400182L;

    private Long id;

    private Long requestId;

    private Long projectSheetId;

    private String title;

    private String bidSheetNum;

    private String sheetType;

    private String tenderType;

    private String tenderMode;

    private Long bidEndTime;

    private Long clarifyEndTime;

    private Long openBidTime;

    private String currency;

    private String tenderMatters;

    private String explanation;

    private String mark;

    private Integer whetherTest;

    private Double budget;

    private String items;

    private Integer approvalProcess;

    private Integer qualificationTender;

    private Integer technicalTender;

    private String rankingRules;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

    private String keyword;

    private List<Long> requestIds;

    private List<Long> projectSheetIds;

    private List<Long> ids;

    private List<String> createdUsers;

}
