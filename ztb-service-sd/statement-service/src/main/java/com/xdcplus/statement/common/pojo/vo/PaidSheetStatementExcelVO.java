package com.xdcplus.statement.common.pojo.vo;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 竞价单(PaidSheet)表VO类(报表)
 *
 * @author Fish.Fei
 * @since 2021-09-07 10:31:20
 */
@Data
@ApiModel(description = "")
public class PaidSheetStatementExcelVO implements Serializable {
    private static final long serialVersionUID = -97118345301844010L;

    @ApiModelProperty("创建人")
    private String createdUserName;

    @ApiModelProperty("竞价编号")
    private String oddNumber;

    @ApiModelProperty("竞价名称")
    private String title;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("出价结构（总额，多项产品）")
    private String bidStructure;

    @ApiModelProperty("竞价方向（正向，反向）")
    private String paidDirection;

    @ApiModelProperty("竞价开始时间")
    private String offerStartTime;

    @ApiModelProperty("竞价截止时间")
    private String offerEndTime;

    @ApiModelProperty("参与供应商数量")
    private Long participateNumber;

    @ApiModelProperty("最优出价")
    private Double bestOffer;

    @ApiModelProperty("当前状态")
    private String requestStatusName;

    @ApiModelProperty("授权供应商")
    private String authorizedVendor;

}
