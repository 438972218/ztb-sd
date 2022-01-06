package com.xdcplus.biz.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 模板题库表(VendorQuestionBank)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-27 14:30:16
 */
@Data
@SuppressWarnings("serial")
public class VendorQuestionBankQuery implements Serializable {
    private static final long serialVersionUID = 501610308886331911L;

    private Long id;

    private String templateName;

    private String type;

    private String name;

    private String balance;

    private String content;

    private String scoreScope;

    private String score;

    private String finalScore;

    private String level;

    private String result;

    private String response;

    private String insDesc;

    private Integer ifPass;

    private Integer ifForce;

    private Integer ifAttachment;

    private String attachmentName;

    private String attachmentUrl;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

    private Long parentId;

}
