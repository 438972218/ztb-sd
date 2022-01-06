package com.xdcplus.statement.common.pojo.vo;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 竞价单(PaidSheet)表VO类(报表)
 *
 * @author Fish.Fei
 * @since 2021-09-07 10:31:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class PaidSheetStatementVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = -97118345301844080L;

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


    @ApiModelProperty("单号")
    private String oddNumber;

    @ApiModelProperty("request标题")
    private String requestTitle;

    @ApiModelProperty("request状态")
    private String requestStatusName;

    @ApiModelProperty("供应商状态")
    private String vendorStatus;

    @ApiModelProperty("request")
    private RequestVO requestVO;

    @ApiModelProperty("物品id")
    private Long paidMaterialId;

    @ApiModelProperty("创建人信息")
    private SysUserInfoVO sysUserInfoVO;

    @ApiModelProperty("项目")
    private ProjectSheetVO projectSheetVO;

    @ApiModelProperty("参与供应商数量")
    private Long participateNumber;

    @ApiModelProperty("最优出价")
    private Double bestOffer;

    @ApiModelProperty("授权供应商")
    private String authorizedVendor;
}
