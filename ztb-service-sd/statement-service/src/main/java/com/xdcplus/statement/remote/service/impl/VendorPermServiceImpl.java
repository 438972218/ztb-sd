package com.xdcplus.statement.remote.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.TypeReference;
import com.xdcplus.statement.remote.VendorPermRemote;
import com.xdcplus.statement.remote.service.VendorPermService;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysRoleVO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.ztb.common.tool.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 烫发服务impl
 *
 * @author Fish.Fei
 * @date 2021/08/17
 */
@Slf4j
@Service("vendorPermService")
public class VendorPermServiceImpl implements VendorPermService {

    @Autowired
    VendorPermRemote vendorPermRemote;

    @Override
    public List<Long> queryByUserId(Long userId) {
        ResponseVO<List<SysRoleVO>> responseVO = vendorPermRemote.queryByUserId(userId);
        if (!NumberConstant.ZERO.equals(responseVO.getCode())) {
            log.error("queryByUserId failed {}", responseVO.getMessage());
            return null;
        }

        List<SysRoleVO> sysRoleVOList = ResponseUtils.getResponse(responseVO, new TypeReference<List<SysRoleVO>>(){});
        if (ObjectUtil.isNotNull(sysRoleVOList) && CollUtil.isNotEmpty(sysRoleVOList)) {
            return sysRoleVOList.stream().map(SysRoleVO::getId).collect(Collectors.toList());
        }

        return null;
    }

    @Override
    public SysUserInfoVO sysUserQueryById(Long id) {
        ResponseVO<SysUserInfoVO> responseVO = vendorPermRemote.sysUserQueryById(id);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {
            log.error("sysUserQueryById failed {}", responseVO.getMessage());
            return null;
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO queryByUserName(String userName) {
        ResponseVO<SysUserInfoVO> responseVO = vendorPermRemote.queryByUserName(userName);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {
            log.error("queryByUserName failed {}", responseVO.getMessage());
            return null;
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }
}
