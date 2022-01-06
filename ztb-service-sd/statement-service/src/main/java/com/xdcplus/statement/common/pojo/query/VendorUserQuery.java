package com.xdcplus.statement.common.pojo.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 供应商用户(VendorUser)表查询条件类
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:25
 */
@Data
@SuppressWarnings("serial")
public class VendorUserQuery implements Serializable {
    private static final long serialVersionUID = 500665675765979286L;

    private Long id;

    private Long vendorId;

    private Long userId;

}
