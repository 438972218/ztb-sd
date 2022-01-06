package com.xdcplus.vendor.remote.service;

import com.xdcplus.ztb.common.remote.domain.perm.dto.RegisterUserDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserRoleInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;

/**
 * 烫发服务
 *
 * @author Fish.Fei
 * @date 2021/08/17
 */
public interface InteriorPermService {

    SysUserInfoVO queryByUserName(String userName);

    SysUserInfoVO registerUser(RegisterUserDTO registerUserDto);

    SysUserInfoVO sysUserQueryById(Long id);

    void insertUserRole(SysUserRoleInfoDTO sysUserRole);

}
