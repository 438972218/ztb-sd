package com.xdcplus.statement.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 竞价单(PaidSheet)表更新入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-09-07 10:31:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class PaidSheetDTO extends BaseBO implements Serializable {
    private static final long serialVersionUID = 698474478633270572L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("requestId")
    private Long requestId;

    @ApiModelProperty("项目id")
    private Long projectSheetId;

    @ApiModelProperty("竞价状态标识")
    private Integer paidStatusMark;

    @ApiModelProperty("竞价状态")
    private String paidStatus;

    @ApiModelProperty("竞价单号")
    private String paidNum;

    @ApiModelProperty("竞价单项目名称")
    private String title;

    @ApiModelProperty("竞价开始时间")
    private Long offerStartTime;

    @ApiModelProperty("竞价截止时间")
    private Long offerEndTime;

    @ApiModelProperty("预算")
    private Double budget;

    @ApiModelProperty("币种")
    private String currency;

    @ApiModelProperty("是否测试（1：是测试，0：不是测试）")
    private Integer whetherTest;

    @ApiModelProperty("竞价方向（正向，反向）")
    private String paidDirection;

    @ApiModelProperty("出价结构（总额，多项产品）")
    private String bidStructure;

    @ApiModelProperty("供应商监控器规则")
    private String vendorMonitorRules;

    @ApiModelProperty("起始价格规则(统一，区分)")
    private String startPriceRule;

    @ApiModelProperty("延时间隔")
    private Integer delayInterval;

    @ApiModelProperty("暂停时间")
    private Long pauseTime;

    @ApiModelProperty("品类")
    private String items;

    @ApiModelProperty("创建人")
    private String createdUser;

    @ApiModelProperty("创建时间")
    private Long createdTime;

    @ApiModelProperty("修改人")
    private String updatedUser;

    @ApiModelProperty("修改时间")
    private Long updatedTime;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("是否已经逻辑删除（0：未删除 1：已删除）")
    private Integer deleted;



    @ApiModelProperty("单号规则ID")
    private Long ruleId;

    @ApiModelProperty("流程ID")
    private Long processId;

    @ApiModelProperty("流程配置版本号")
    private String configVersion;

    @ApiModelProperty("userId")
    private Long userId;

    @ApiModelProperty("用戶名称")
    private String name;

    @ApiModelProperty("keyword")
    private String keyword;

    @ApiModelProperty("requestIds")
    private List<Long> requestIds;

    @ApiModelProperty("表单类型ID")
    private Long typeId;

    private List<PaidMaterialDTO> paidMaterialDTOS;

    private List<PaidVendorDTO> paidVendorDTOS;

    private List<PaidAttachmentDTO> paidAttachmentDTOS;

}
