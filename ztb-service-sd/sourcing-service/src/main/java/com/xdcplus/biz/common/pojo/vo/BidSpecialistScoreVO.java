package com.xdcplus.biz.common.pojo.vo;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 招标专家评分(BidSpecialistScore)表VO类
 *
 * @author Fish.Fei
 * @since 2021-10-27 13:48:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class BidSpecialistScoreVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = -78627245572703186L;

    @ApiModelProperty("供应商id")
    private Long bidVendorId;

    @ApiModelProperty("供应商名称")
    private String bidVendorName;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("标类型")
    private String bidType;

    @ApiModelProperty("是否查看")
    private Integer whetherView;

    @ApiModelProperty("查看时间")
    private Long viewTime;

    @ApiModelProperty("评分")
    private Double score;

    @ApiModelProperty("评论")
    private String comment;

    @ApiModelProperty("轮次")
    private Integer round;

    @ApiModelProperty("状态（最新/历史）")
    private String status;

    @ApiModelProperty("用户")
    private SysUserInfoVO sysUserInfoVO;
    @ApiModelProperty("评论时间")
    private Long commentTime;

}
