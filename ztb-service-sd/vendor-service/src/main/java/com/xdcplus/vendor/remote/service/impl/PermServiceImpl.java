package com.xdcplus.vendor.remote.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.TypeReference;
import com.xdcplus.vendor.remote.PermRemote;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.dto.RegisterUserDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserRoleInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.ztb.common.tool.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 烫发服务impl
 *
 * @author Fish.Fei
 * @date 2021/08/17
 */
@Slf4j
@Service("permService")
public class PermServiceImpl implements PermService {

    @Autowired
    PermRemote permRemote;

    @Override
    public SysUserInfoVO queryByUserName(String userName) {
        ResponseVO<SysUserInfoVO> responseVO = permRemote.queryByUserName(userName);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("queryByUserName failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO registerUser(RegisterUserDTO registerUserDto) {
        ResponseVO<SysUserInfoVO> responseVO = permRemote.registerUser(registerUserDto);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("registerUser failed");
//            return null;
            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO registerUserNotSendEmail(RegisterUserDTO registerUserDto) {
        ResponseVO<SysUserInfoVO> responseVO = permRemote.registerUserNotSendEmail(registerUserDto);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("registerUserNotSendEmail failed");
//            return null;
            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO registerUserCover(RegisterUserDTO registerUserDto) {
        ResponseVO<SysUserInfoVO> responseVO = permRemote.registerUserCover(registerUserDto);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("registerUserNotSendEmail failed");
//            return null;
            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO registerUserNotSendEmailAndCover(RegisterUserDTO registerUserDto) {
        ResponseVO<SysUserInfoVO> responseVO = permRemote.registerUserNotSendEmailAndCover(registerUserDto);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("registerUserNotSendEmail failed");
//            return null;
            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO sysUserQueryById(Long id) {
        ResponseVO<SysUserInfoVO> responseVO = permRemote.sysUserQueryById(id);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("sysUserQueryById failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public void insertUserRole(SysUserRoleInfoDTO sysUserRole) {
        ResponseVO responseVO = permRemote.insertUserRole(sysUserRole);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("insertUserRole failed");
            return ;
//            throw new ZtbWebException(ResponseEnum.API_USER_ROLE_INSERT_FAIL, responseVO.getMessage());
        }
    }

    @Override
    public List<SysUserInfoVO> queryAllVendorUser() {
        ResponseVO<List<SysUserInfoVO>> responseVO = permRemote.queryAllVendorUser();
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("sysUserQueryById failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, new TypeReference<List<SysUserInfoVO>>(){});
    }

    @Override
    public PageVO<SysUserInfoVO> querySysUserVOPageVO(SysUserInfoDTO sysUserInfoDTO) {
        ResponseVO<PageVO<SysUserInfoVO>> responseVO = permRemote.querySysUserVOPageVO(sysUserInfoDTO);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("sysUserQueryById failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, new TypeReference<PageVO<SysUserInfoVO>>(){});
    }

}
