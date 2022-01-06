package com.xdcplus.statement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.statement.common.constants.ZtbConstant;
import com.xdcplus.statement.common.pojo.query.ProjectSheetQuery;
import com.xdcplus.statement.generator.impl.ProjectSheetBaseServiceImpl;
import com.xdcplus.statement.mapper.ProjectSheetMapper;
import com.xdcplus.statement.common.pojo.entity.ProjectSheet;
import com.xdcplus.statement.remote.service.PermService;
import com.xdcplus.statement.common.pojo.dto.*;
import com.xdcplus.statement.common.pojo.vo.ProjectMemberVO;
import com.xdcplus.statement.common.pojo.vo.ProjectSheetVO;
import com.xdcplus.statement.service.*;
import com.xdcplus.ztb.common.page.PageUtils;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目(ProjectSheet)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-20 16:31:01
 */
@Slf4j
@Service("projectSheetService")
public class ProjectSheetServiceImpl extends ProjectSheetBaseServiceImpl<ProjectSheet, ProjectSheetVO, ProjectSheet, ProjectSheetMapper> implements ProjectSheetService {

    @Autowired
    ProjectMemberService projectMemberService;

    @Autowired
    PermService permService;

    @Override
    public PageVO<ProjectSheetVO> queryProjectSheetWithUser(ProjectSheetFilterDTO projectSheetFilterDTO) {
        projectSheetFilterDTO.setDeleted(0);
        PageVO<ProjectSheetVO> pageVO = new PageVO<>();

        if (projectSheetFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(projectSheetFilterDTO.getCurrentPage(), projectSheetFilterDTO.getPageSize(),
                    projectSheetFilterDTO.getOrderType(), projectSheetFilterDTO.getOrderField());
        }

        ProjectSheetQuery projectSheetQuery = BeanUtil.copyProperties(projectSheetFilterDTO, ProjectSheetQuery.class);
        List<ProjectSheet> projectSheetList = projectSheetMapper.queryProjectSheetWithUser(projectSheetQuery);

        PageInfo<ProjectSheet> pageInfo = new PageInfo<>(projectSheetList);
        PropertyUtils.copyProperties(pageInfo, pageVO, this.objectConversion(projectSheetList));

        return pageVO;
    }

    @Override
    public PageVO<ProjectSheetVO> queryProjectSheetWithUser1(ProjectSheetFilterDTO projectSheetFilterDTO) {
        List<ProjectSheetVO> projectSheetVOS = queryProjectSheetVOList(projectSheetFilterDTO);
        if (CollectionUtil.isEmpty(projectSheetVOS)) {
            return null;
        }

        //过滤
        List<ProjectSheetVO> projectSheetList = filterProjectSheetList(projectSheetVOS, projectSheetFilterDTO.getUserId());
        if (CollectionUtil.isEmpty(projectSheetList)) {
            return null;
        }

        return PageUtils.getPageVO(projectSheetList, projectSheetFilterDTO.getCurrentPage(),
                projectSheetFilterDTO.getPageSize() == null ? 0 : projectSheetFilterDTO.getPageSize());
    }

    @Override
    public PageVO<ProjectSheetVO> queryProjectSheetWithCreatedUser(ProjectSheetFilterDTO projectSheetFilterDTO) {
        PageVO<ProjectSheetVO> projectSheetVOPageVO=queryProjectSheet(projectSheetFilterDTO);
        List<ProjectSheetVO> projectSheetVOS = projectSheetVOPageVO.getRecords();
        if(CollectionUtil.isEmpty(projectSheetVOS)){
            return projectSheetVOPageVO;
        }
        for (ProjectSheetVO projectSheetVO : projectSheetVOS) {
            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(projectSheetVO.getCreatedUser());
            projectSheetVO.setSysUserInfoVO(sysUserInfoVO);
        }
        projectSheetVOPageVO.setRecords(projectSheetVOS);

        return projectSheetVOPageVO;
    }

    private List<ProjectSheetVO> filterProjectSheetList(List<ProjectSheetVO> projectSheetVOS, Long userId) {
        for (ProjectSheetVO projectSheetVO : projectSheetVOS) {
            projectSheetVO.setSysUserInfoVO(permService.queryByUserName(projectSheetVO.getCreatedUser()));
        }

        List<ProjectSheetVO> projectSheetVOS1 = CollectionUtil.newArrayList();

//        List<SysUserInfoVO> adminSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.ADMIN_ROLE_ID);
//        List<SysUserInfoVO> auditorSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.AUDITOR_ROLE_ID);
//        //判断是否是管理员或审计员
//        if ( (CollectionUtil.isNotEmpty(adminSysUserInfoVOS) &&
//                adminSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(userId)) ||
//             (CollectionUtil.isNotEmpty(auditorSysUserInfoVOS) &&
//                auditorSysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList()).contains(userId))) {
//            return projectSheetVOS;
//        }

        //判断userId是否是审计
        List<SysUserInfoVO> sysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.AUDITOR_ROLE_ID);
        if(CollectionUtil.isNotEmpty(sysUserInfoVOS)){
            List<Long> userIds = sysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList());
            if(userIds.contains(userId)){
                return projectSheetVOS;
            }
        }

        //判断userId是否是管理员
        List<SysUserInfoVO> adminSysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.ADMIN_ROLE_ID);
        if(CollectionUtil.isNotEmpty(adminSysUserInfoVOS)){
            List<Long> userIds = sysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList());
            if(userIds.contains(userId)){
                return projectSheetVOS;
            }
        }

        //自己及下属
        SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(userId);
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("sysUserInfoVO is not exists");
            return null;
        }
        List<String> subordinateUserNames = permService.getSubordinateUserNames(sysUserInfoVO.getUserName());

        for (ProjectSheetVO projectSheetVO : projectSheetVOS) {
            //项目下成员
            ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
            projectMemberFilterDTO.setProjectId(projectSheetVO.getId());
            List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);
            if (CollectionUtil.isNotEmpty(projectMemberVOS)) {
                List<Long> userIds = projectMemberVOS.stream().map(ProjectMemberVO::getUserId).collect(Collectors.toList());
                if (userIds.contains(userId)) {
                    projectSheetVOS1.add(projectSheetVO);
                    continue;
                }
            }

            if(userId==null){
                continue;
            }
            //自己及下属
            if (subordinateUserNames.contains(projectSheetVO.getCreatedUser())) {
                projectSheetVOS1.add(projectSheetVO);
            }

        }

        return projectSheetVOS1;
    }


}
