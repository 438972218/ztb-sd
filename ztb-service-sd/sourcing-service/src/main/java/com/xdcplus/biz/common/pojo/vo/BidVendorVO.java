package com.xdcplus.biz.common.pojo.vo;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 招标投标方(BidVendor)表VO类
 *
 * @author Fish.Fei
 * @since 2021-10-15 13:27:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidVendorVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = -34252092905866827L;

    @ApiModelProperty("招标单id")
    private Long bidSheetId;

    @ApiModelProperty("供应商id")
    private Long vendorId;

    @ApiModelProperty("供应商用户id")
    private Long vendorUserId;

    @ApiModelProperty("投标方编码")
    private String vendorCode;

    @ApiModelProperty("投标方名称")
    private String vendorName;

    @ApiModelProperty("投标方状态（待参与，未参与，待回复，已回复）")
    private String vendorStatus;

    @ApiModelProperty("供应商用户名称")
    private String vendorUserName;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("重开RFQ（未发送，受邀）")
    private String againStatus;

    @ApiModelProperty("回复时间")
    private Long replyTime;

    @ApiModelProperty("供应商已查看RFQ时间")
    private Long checkTime;

    @ApiModelProperty("轮次")
    private Integer round;

    @ApiModelProperty("资质附件数量")
    private Integer qualificationAttQuantity;

    @ApiModelProperty("技术附件数量")
    private Integer technologyAttQuantity;

    @ApiModelProperty("资质是否查看")
    private Integer qualificationView;

    @ApiModelProperty("技术是否查看")
    private Integer technologyView;

    @ApiModelProperty("商业是否查看")
    private Integer commerceView;

    @ApiModelProperty("总价")
    private Double totalPrice;

    @ApiModelProperty("资质总评分")
    private Double qualificationScore;

    @ApiModelProperty("技术总评分")
    private Double technologyScore;

    @ApiModelProperty("商业总评分")
    private Double commerceScore;

    @ApiModelProperty("总评分")
    private Double totalScore;

    @ApiModelProperty("确认价格")
    private Double confirmedPrice;

    @ApiModelProperty("供应商信息")
    private VendorVO vendorVO;

    @ApiModelProperty("专家评分信息")
    private BidSpecialistScoreVO bidSpecialistScoreVO;


}
