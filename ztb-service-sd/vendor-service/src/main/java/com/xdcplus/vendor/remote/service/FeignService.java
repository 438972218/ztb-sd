package com.xdcplus.vendor.remote.service;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;

/**
 * feign 调用处理
 *
 * @author Rong.Jia
 * @date 2021/08/17 15:09:46
 */
public interface FeignService {

    /**
     * 查询用户信息通过用户的名字
     *
     * @param userName 用户名
     * @return {@link SysUserInfoVO}
     */
    SysUserInfoVO findUserInfoByUserName(String userName);

    /**
     * 获取sys角色马克通过用户的名字
     *
     * @param userName 用户名
     * @return {@link Boolean} true: 内部，false: 外部
     */
    Boolean getSysRoleMarkByUserName(String userName);



}
