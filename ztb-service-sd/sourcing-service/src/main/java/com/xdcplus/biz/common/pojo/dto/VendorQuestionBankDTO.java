package com.xdcplus.biz.common.pojo.dto;

import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 模板题库表(VendorQuestionBank)表更新入参DTO类
 *
 * @author Fish.Fei
 * @since 2021-09-27 14:30:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class VendorQuestionBankDTO extends BaseBO implements Serializable {
    private static final long serialVersionUID = 980051125308834190L;

    @ApiModelProperty("信息主键")
    private Long id;

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

    @ApiModelProperty("父id")
    private Long parentId;

}
