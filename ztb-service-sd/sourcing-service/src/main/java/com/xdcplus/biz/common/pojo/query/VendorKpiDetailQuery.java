package com.xdcplus.biz.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商绩效考核表明细(VendorKpiDetail)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-28 17:37:06
 */
@Data
@SuppressWarnings("serial")
public class VendorKpiDetailQuery implements Serializable {
    private static final long serialVersionUID = -60165398352818040L;

    private Long id;

    private Long vendorKpiId;

    private Long parentId;

    private String type;

    private String name;

    private String content;

    private String balance;

    private String scoreScope;

    private String score;

    private String insDesc;

    private String createdUser;

    private Long createdTime;

    private String updatedUser;

    private Long updatedTime;

    private String description;

    private Integer version;

    private Integer deleted;

}
