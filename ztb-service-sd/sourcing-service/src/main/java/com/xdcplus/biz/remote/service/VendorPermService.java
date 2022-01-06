package com.xdcplus.biz.remote.service;

import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;

import java.util.List;

/**
 * 烫发服务
 *
 * @author Fish.Fei
 * @date 2021/08/17
 */
public interface VendorPermService {

    /**
     * 根据用户id查询角色
     * @param userId
     * @return
     */
    List<Long> queryByUserId(Long userId);

    /**
     * 查询通过id
     *
     * @param id id
     * @return {@link SysUserInfoVO} 用户信息vo
     */
    SysUserInfoVO sysUserQueryById(Long id);

    SysUserInfoVO queryByUserName(String userName);

}
