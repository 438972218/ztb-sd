package com.xdcplus.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.ProjectMemberDTO;
import com.xdcplus.biz.generator.impl.ProjectMemberBaseServiceImpl;
import com.xdcplus.biz.mapper.ProjectMemberMapper;
import com.xdcplus.biz.common.pojo.entity.ProjectMember;
import com.xdcplus.biz.common.pojo.vo.ProjectMemberVO;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.service.ProjectMemberService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysEmployeeVO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 项目成员信息(ProjectMember)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-24 09:40:42
 */
@Slf4j
@Service("projectMemberService")
public class ProjectMemberServiceImpl extends ProjectMemberBaseServiceImpl<ProjectMember, ProjectMemberVO, ProjectMember, ProjectMemberMapper> implements ProjectMemberService {

    @Autowired
    PermService permService;

    @Override
    public List<ProjectMemberDTO> combineProjectMember(String userName) {
        List<ProjectMemberDTO> projectMemberDTOS = CollectionUtil.newArrayList();
        //新增创建人
        SysUserInfoVO sysUserInfoVO = permService.queryByUserName(userName);
        SysEmployeeVO sysEmployeeVO = permService.getEmployeeVoByUserName(userName);
        combineProjectMember(projectMemberDTOS, sysUserInfoVO, sysEmployeeVO);

//        //新增部门负责人
//        GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO getDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO =
//                permService.getDepartmentManagerEmployeeVoAndSysUserVoByUserName(userName);
//        SysEmployeeVO sysEmployeeVOManager = getDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO.getSysEmployeeVo();
//        SysUserInfoVO sysUserInfoVOManager = getDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO.getSysUserVo();
//        if (ObjectUtil.isEmpty(sysEmployeeVOManager) ||
//                ObjectUtil.isEmpty(sysUserInfoVOManager)) {
//            log.error("saveProjectMember() GetDepartmentManagerEmployeeVoAndSysUserVoByUserNameVO select faild");
//            throw new ZtbWebException(ResponseEnum.USER_DEPARTMENT_MANAGER_SELECT_FAILD);
//        }
//        combineProjectMember(projectMemberDTOS, sysUserInfoVOManager.getId(), userName, sysEmployeeVOManager);
//
        //新增管理员
        SysEmployeeVO sysEmployeeVOAdmin = permService.getEmployeeVoByUserName("admin");
        SysUserInfoVO sysUserInfoVOAdmin = permService.queryByUserName("admin");
        combineProjectMember(projectMemberDTOS, sysUserInfoVOAdmin, sysEmployeeVOAdmin);

        //新增审计员
        List<SysUserInfoVO> sysUserInfoVOS = permService.getSysUserByRoleId(ZtbConstant.AUDITOR_ROLE_ID);
        if(CollectionUtil.isNotEmpty(sysUserInfoVOS)){
            for (SysUserInfoVO userInfoVO : sysUserInfoVOS) {
                SysEmployeeVO sysEmployeeVOAuditor = permService.getEmployeeVoByUserName(userInfoVO.getUserName());
                SysUserInfoVO sysUserInfoVOAuditor = permService.queryByUserName(userInfoVO.getUserName());
                combineProjectMember(projectMemberDTOS, sysUserInfoVOAuditor, sysEmployeeVOAuditor);
            }
        }

        if(CollectionUtil.isEmpty(projectMemberDTOS)){
            log.error("combineProjectMember select faild");
            throw new ZtbWebException(ResponseEnum.EMPLOYEE_IS_NOT_EXIST);
        }

        projectMemberDTOS = projectMemberDTOS.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(ProjectMemberDTO::getUserId))), ArrayList::new)
        );

        return projectMemberDTOS;
    }

    private void combineProjectMember(List<ProjectMemberDTO> projectMemberDTOS,
                                      SysUserInfoVO sysUserInfoVO, SysEmployeeVO sysEmployeeVO) {
        ProjectMemberDTO projectMemberDTO = new ProjectMemberDTO();
//        projectMemberDTO.setProjectId(projectSheetId);
        projectMemberDTO.setUserId(sysUserInfoVO.getId());
        projectMemberDTO.setName(sysUserInfoVO.getName());
        projectMemberDTO.setEmail(sysUserInfoVO.getMail());
        projectMemberDTO.setDepartment(sysEmployeeVO.getDepartmentName());
        projectMemberDTO.setPost(sysEmployeeVO.getPositionName());
        projectMemberDTO.setCreatedUser(sysUserInfoVO.getUserName());
        projectMemberDTOS.add(projectMemberDTO);
    }
}
