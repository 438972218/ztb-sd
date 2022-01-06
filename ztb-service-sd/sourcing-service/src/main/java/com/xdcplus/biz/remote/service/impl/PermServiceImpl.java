package com.xdcplus.biz.remote.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.TypeReference;
import com.xdcplus.biz.remote.PermRemote;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.*;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
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
@Service("permService")
public class PermServiceImpl implements PermService {

    @Autowired
    PermRemote permRemote;

    @Override
    public SysCompanyVO sysCompanyQueryById(Long id) {
        ResponseVO<SysCompanyVO> responseVO = permRemote.sysCompanyQueryById(id);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("sysCompanyQueryById failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_COMPANY_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysCompanyVO.class);
    }

    @Override
    public List<SysCompanyVO> getDepartmentTree() {
        ResponseVO<List<SysCompanyVO>> responseVO = permRemote.getDepartmentTree();
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("getDepartmentTree failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_COMPANY_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, new TypeReference<List<SysCompanyVO>>(){});
    }

    @Override
    public ResponseVO judgeGroupCompany(Long id) {
        ResponseVO responseVO = permRemote.judgeGroupCompany(id);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("judgeGroupCompany failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_COMPANY_SELECT_FAIL, responseVO.getMessage());
        }
        return responseVO;
    }

    @Override
    public List<Long> queryByUserId(Long userId) {
        ResponseVO<List<SysRoleVO>> responseVO = permRemote.queryByUserId(userId);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("queryByUserId failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_ROLE_SELECT_FAIL, responseVO.getMessage());
        }
        List<SysRoleVO> sysRoleVOS = ResponseUtils.getResponse(responseVO, new TypeReference<List<SysRoleVO>>(){});

        if (CollUtil.isEmpty(sysRoleVOS)) {
            log.error("queryByUserId() role found failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_ROLE_SELECT_FAIL);
        }
        List<Long> roleIds = sysRoleVOS.stream().map(s -> s.getId()).collect(Collectors.toList());
        return roleIds;
    }

    @Override
    public SysUserInfoVO sysUserQueryById(Long id) {
        ResponseVO<SysUserInfoVO> responseVO = permRemote.sysUserQueryById(id);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("sysUserQueryById failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysUserInfoVO queryByUserName(String userName) {
        ResponseVO<SysUserInfoVO> responseVO = permRemote.queryByUserName(userName);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("queryByUserName failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysUserInfoVO.class);
    }

    @Override
    public SysEmployeeVO getEmployeeVoByUserName(String userName) {
        ResponseVO<SysEmployeeVO> responseVO = permRemote.getEmployeeVoByUserName(userName);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("getEmployeeVoByUserName failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, SysEmployeeVO.class);
    }

    @Override
    public GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO getDepartmentManagerEmployeeVoAndSysUserVoByUserName(String userName) {
        ResponseVO<GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO> responseVO = permRemote.getDepartmentManagerEmployeeVoAndSysUserVoByUserName(userName);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("getDepartmentManagerEmployeeVoAndSysUserVoByUserName failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO.class);
    }

    @Override
    public List<String> getSubordinateUserNames(String userName) {
        ResponseVO<List<String>> responseVO = permRemote.getSubordinateUserNames(userName);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("getSubordinateUserNames failed {}",responseVO.getMessage());
            return null;
//            throw new ZtbWebException(ResponseEnum.API_USER_SELECT_FAIL, responseVO.getMessage());
        }
        return ResponseUtils.getResponse(responseVO, new TypeReference<List<String>>(){});
    }

    @Override
    public List<SysUserInfoVO> getSysUserByRoleId(Long roleId) {
        ResponseVO<List<SysUserInfoVO>> responseVO = permRemote.getSysUserByRoleId(roleId);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("getSysUserByRoleId failed {}",responseVO.getMessage());
            return null;
        }
        return ResponseUtils.getResponse(responseVO, new TypeReference<List<SysUserInfoVO>>(){});
    }

}
