package com.xdcplus.vendor.remote.service.impl;

import cn.hutool.core.lang.Validator;
import com.xdcplus.vendor.common.enums.InOutEnum;
import com.xdcplus.vendor.remote.PermRemote;
import com.xdcplus.vendor.remote.service.FeignService;
import com.xdcplus.ztb.common.cache.RedisUtils;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.ztb.common.tool.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * feign 调用处理
 *
 * @author Rong.Jia
 * @date 2021/08/17 15:09:46
 */
@Slf4j
@Service
public class FeignServiceImpl implements FeignService {

    @Autowired
    private PermRemote permRemote;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public SysUserInfoVO findUserInfoByUserName(String userName) {
        return getOfferUser(userName, NumberConstant.ZERO);
    }

    @Override
    public Boolean getSysRoleMarkByUserName(String userName) {

        String sysRoleMark = getSysRoleMark(userName, NumberConstant.ZERO);
        return InOutEnum.External.name().equalsIgnoreCase(sysRoleMark) ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * 获取用户角色标识
     *
     * @param userName  用户名
     * @param retryCount 重试计数
     * @return {@link String} 角色标识
     */
    private String getSysRoleMark(String userName, int retryCount) {

        if (retryCount >= NumberConstant.THREE){
            log.error("getSysRoleMark() Maximum number of retries index : {}", retryCount);
            return null;
        }

        ResponseVO<String> responseVO = permRemote.getSysRoleMarkByUserName(userName);
        if (Validator.equal(responseVO.getCode(), NumberConstant.ZERO)) {
            return ResponseUtils.getResponse(responseVO);
        }

        retryCount++;
        getSysRoleMark(userName, retryCount);

        return null;
    }

    /**
     * 获取供应商用户
     *
     * @param offerUser  供应商用户名
     * @param retryCount 重试计数
     * @return {@link SysUserInfoVO} 用户
     */
    private SysUserInfoVO getOfferUser(String offerUser, int retryCount) {

        if (retryCount >= NumberConstant.THREE){
            log.error("getOfferUser() Maximum number of retries index : {}", retryCount);
            return null;
        }

        ResponseVO<SysUserInfoVO> responseVO = permRemote.queryByUserName(offerUser);
        if (Validator.equal(responseVO.getCode(), NumberConstant.ZERO)) {
            return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
        }

        retryCount++;
        getOfferUser(offerUser, retryCount);

        return null;
    }
















}
