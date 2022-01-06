package com.xdcplus.ztb.common.remote.domain.perm.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Fish.Fei
 */
@Data
@Builder
public class SysUserRoleInfoDTO implements Serializable {
    private static final long serialVersionUID = 3925834477733875768L;
    /**
     * 主键
     */
    private Long id;
    /**
     * 创建人
     */
    private String createdUser;
    /**
     * 创建时间
     */
    private Long createdTime;
    /**
     * 角色编号
     */
    private Long roleId;
    /**
     * 用户id
     */
    private Long userId;
}
