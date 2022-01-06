package com.xdcplus.vendor.remote.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.xdcplus.vendor.remote.InteriorPermRemote;
import com.xdcplus.vendor.remote.PermRemote;
import com.xdcplus.vendor.remote.service.InteriorPermService;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.dto.RegisterUserDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserRoleInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.ztb.common.tool.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 烫发服务impl
 *
 * @author Fish.Fei
 * @date 2021/08/17
 */
@Slf4j
@Service("interiorPermService")
public class InteriorPermServiceImpl implements InteriorPermService {

    @Autowired
    InteriorPermRemote interiorPermRemote;

    @Override
    public SysUserInfoVO queryByUserName(String userName) {
        ResponseVO<SysUserInfoVO> responseVO = interiorPermRemote.queryByUserName(userName);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("queryByUserName failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO registerUser(RegisterUserDTO registerUserDto) {
        ResponseVO<SysUserInfoVO> responseVO = interiorPermRemote.registerUser(registerUserDto);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("registerUser failed");
//            return null;
            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO sysUserQueryById(Long id) {
        ResponseVO<SysUserInfoVO> responseVO = interiorPermRemote.sysUserQueryById(id);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("sysUserQueryById failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public void insertUserRole(SysUserRoleInfoDTO sysUserRole) {
        ResponseVO responseVO = interiorPermRemote.insertUserRole(sysUserRole);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("insertUserRole failed");
            return ;
//            throw new ZtbWebException(ResponseEnum.API_USER_ROLE_INSERT_FAIL, responseVO.getMessage());
        }
    }

}
