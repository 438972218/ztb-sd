package com.xdcplus.statement.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * bid招标单(BidSheet)表VO类(报表)
 *
 * @author Fish.Fei
 * @since 2021-09-17 17:38:15
 */
@Data
@ApiModel(description = "")
public class BidSheetStatementExcelVO implements Serializable {
    private static final long serialVersionUID = 512638065256848411L;

    @ApiModelProperty("招标员")
    private String createdUserName;

    @ApiModelProperty("招标单号")
    private String oddNumber;

    @ApiModelProperty("主题")
    private String title;

    @ApiModelProperty("所属项目")
    private String projectName;

    @ApiModelProperty("投标截止时间")
    private String bidEndTime;

    @ApiModelProperty("状态")
    private String requestStatusName;

    @ApiModelProperty("已回复数量")
    private Long repliesNumber;

    @ApiModelProperty("授权供应商")
    private String authorizedVendor;

}
