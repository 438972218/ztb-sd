package com.xdcplus.biz.common.pojo.vo;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.pojo.bo.BaseBO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 供应商用户(VendorUser)表VO类
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "")
@SuppressWarnings("serial")
public class VendorUserVO extends BaseBO implements Serializable {
    private static final long serialVersionUID = 568996914783502578L;

    @ApiModelProperty("供应商id")
    private Long vendorId;

    @ApiModelProperty("用户id")
    private Long userId;

    private SysUserInfoVO sysUserInfoVO;

}
