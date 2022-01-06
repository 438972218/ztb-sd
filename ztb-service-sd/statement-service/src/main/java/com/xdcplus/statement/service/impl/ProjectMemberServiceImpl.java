package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.ProjectMemberBaseServiceImpl;
import com.xdcplus.statement.mapper.ProjectMemberMapper;
import com.xdcplus.statement.common.pojo.entity.ProjectMember;
import com.xdcplus.statement.common.pojo.vo.ProjectMemberVO;
import com.xdcplus.statement.service.ProjectMemberService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目成员信息(ProjectMember)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-24 09:40:42
 */
@Slf4j
@Service("projectMemberService")
public class ProjectMemberServiceImpl extends ProjectMemberBaseServiceImpl<ProjectMember, ProjectMemberVO, ProjectMember, ProjectMemberMapper> implements ProjectMemberService {

}
