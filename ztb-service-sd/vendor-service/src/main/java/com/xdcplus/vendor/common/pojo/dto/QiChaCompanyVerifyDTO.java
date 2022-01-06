package com.xdcplus.vendor.common.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class QiChaCompanyVerifyDTO implements Serializable {
    private static final long serialVersionUID = 781064976740844308L;

    @ApiModelProperty("统一社会信用代码")
    private String creditCode;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("法定代表人名称")
    private String operName;
}
