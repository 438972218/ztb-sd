package com.xdcplus.vendorperm.perm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户信息表(SysUser)实体类
 *
 * @author Bullion.Yan
 * @since 2021-06-28 13:10:09
 */
@Data
@Builder
public class UserPasswordVO implements Serializable {
    private static final long serialVersionUID = -22340563453816945L;

    private String vendorName;

    private String userName;

    private String password;

    private String encryptPassword;
}
