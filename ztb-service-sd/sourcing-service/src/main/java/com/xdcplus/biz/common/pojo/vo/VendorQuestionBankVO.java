package com.xdcplus.biz.common.pojo.vo;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 模板题库表(VendorQuestionBank)表VO类
 *
 * @author Fish.Fei
 * @since 2021-09-27 14:30:16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class VendorQuestionBankVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = -77862420995524462L;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("问题类型")
    private String type;

    @ApiModelProperty("问题名称")
    private String name;

    @ApiModelProperty("权重")
    private String balance;

    @ApiModelProperty("问题内容")
    private String content;

    @ApiModelProperty("得分范围")
    private String scoreScope;

    @ApiModelProperty("评分")
    private String score;

    @ApiModelProperty("最终得分")
    private String finalScore;

    @ApiModelProperty("等级")
    private String level;

    @ApiModelProperty("结果")
    private String result;

    @ApiModelProperty("问题答复")
    private String response;

    @ApiModelProperty("问题描述")
    private String insDesc;

    @ApiModelProperty("是否通过")
    private Integer ifPass;

    @ApiModelProperty("是否强制")
    private Integer ifForce;

    @ApiModelProperty("是否需要上传附件")
    private Integer ifAttachment;

    @ApiModelProperty("附件名称")
    private String attachmentName;

    @ApiModelProperty("附件地址")
    private String attachmentUrl;

    @ApiModelProperty("父id")
    private Long parentId;

    @ApiModelProperty("子项")
    private List<VendorQuestionBankVO> childVendorQuestionBankVOS;

}
