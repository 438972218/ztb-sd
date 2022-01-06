package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctc.wstx.util.DataUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.*;
import com.xdcplus.biz.common.pojo.entity.PaidSheet;
import com.xdcplus.biz.common.pojo.entity.ProjectAttachment;
import com.xdcplus.biz.common.pojo.query.ProjectSheetQuery;
import com.xdcplus.biz.common.pojo.vo.*;
import com.xdcplus.biz.generator.impl.ProjectSheetBaseServiceImpl;
import com.xdcplus.biz.mapper.ProjectSheetMapper;
import com.xdcplus.biz.common.pojo.entity.ProjectSheet;
import com.xdcplus.biz.remote.service.IeService;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.service.*;
import com.xdcplus.ztb.common.cache.RedisUtils;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.page.PageUtils;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.sourcing.PaidSheetInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.ExtraMailNotificationInfoDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.MailBeanInfo;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.CacheConstant;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
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
    ProjectAttachmentService projectAttachmentService;

    @Autowired
    ProjectMemberService projectMemberService;

    @Autowired
    BidSheetService bidSheetService;

    @Autowired
    PaidSheetService paidSheetService;

    @Autowired
    PermService permService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    DictionaryService dictionaryService;

    @Autowired
    IeService ieService;

    @Override
    public ProjectSheetVO saveProjectSheetReturnVO(ProjectSheetDTO projectSheetDTO) {
        judgeTitle(projectSheetDTO.getTitle());

        List<ProjectMemberDTO> projectMemberDTOS = projectMemberService.combineProjectMember(projectSheetDTO.getCreatedUser());

        ProjectSheet projectSheet = projectSheetMapper.selectById(projectSheetDTO.getId());
        if (ObjectUtil.isNotNull(projectSheet)) {
            log.error("saveProjectSheetReturnVO() The ProjectSheet already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        projectSheet = new ProjectSheet();
        BeanUtil.copyProperties(projectSheetDTO, projectSheet);

        //PRO+年月日+3位流水

        String codeNumber = redisUtils.get(CacheConstant.PROJECT_CODE_NUMBER, new TypeReference<String>(){});
        if(codeNumber==null){
            StringBuffer buffer=new StringBuffer();
            String date = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
            String springWater=String.format("%03d", 1);
            redisUtils.set(CacheConstant.PROJECT_CODE_NUMBER, JSON.toJSONString(springWater));
            buffer.append("PRO").append(date).append(springWater);
            projectSheet.setCode(buffer.toString());
        }else{
            int codeNumberInt = Integer.parseInt(codeNumber);
            if(codeNumberInt == ZtbConstant.CODE_NUMBER_999){
                codeNumberInt=NumberConstant.ONE;
            }else{
                codeNumberInt+=NumberConstant.ONE;
            }
            StringBuffer buffer=new StringBuffer();
            String date = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
            String springWater=String.format("%03d", codeNumberInt);
            redisUtils.set(CacheConstant.PROJECT_CODE_NUMBER, JSON.toJSONString(springWater));
            buffer.append("PRO").append(date).append(springWater);
            projectSheet.setCode(buffer.toString());
        }
        projectSheet.setCreatedTime(DateUtil.current());
        boolean result = this.save(projectSheet);
        ProjectSheetVO projectSheetVO = BeanUtil.copyProperties(projectSheet, ProjectSheetVO.class);
        if(projectSheetVO.getCreatedUser()!=null){
            projectSheetVO.setSysUserInfoVO(permService.queryByUserName(projectSheetVO.getCreatedUser()));
        }

        projectMemberDTOS.forEach(projectMemberDTO -> projectMemberDTO.setProjectId(projectSheetVO.getId()));
        projectMemberService.saveOrUpdateBatchByDTOList(projectMemberDTOS);

        if (result) {
            return projectSheetVO;
        } else {
            return null;
        }
    }

    @Override
    public ProjectSheetVO showProjectSheet(Long id) {
        ProjectSheetVO projectSheetVO=queryProjectSheetById(id);
        if(ObjectUtil.isEmpty(projectSheetVO)){
            return null;
        }

        if (projectSheetVO.getCreatedUser() != null) {
            //创建人
            SysUserInfoVO sysUserInfoVO = permService.queryByUserName(projectSheetVO.getCreatedUser());
            projectSheetVO.setSysUserInfoVO(sysUserInfoVO);
        }

        ProjectAttachmentFilterDTO projectAttachmentFilterDTO=new ProjectAttachmentFilterDTO();
        projectAttachmentFilterDTO.setProjectId(projectSheetVO.getId());
        List<ProjectAttachmentVO> projectAttachmentVOS = projectAttachmentService.queryProjectAttachmentVOList(projectAttachmentFilterDTO);
        projectSheetVO.setProjectAttachmentVOS(projectAttachmentVOS);

        ProjectMemberFilterDTO projectMemberFilterDTO=new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setProjectId(projectSheetVO.getId());
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);
        projectSheetVO.setProjectMemberVOS(projectMemberVOS);

        return projectSheetVO;
    }

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

    @Override
    public Boolean deleteProjectSheet(Long id) {
        int bidSheetCount = bidSheetService.selectCountByProjectId(id);
        int paidSheetCount = paidSheetService.selectCountByProjectId(id);
        if(!NumberConstant.ZERO.equals(bidSheetCount) || !NumberConstant.ZERO.equals(paidSheetCount)){
            log.error("deleteProjectSheet() this ProjectSheet exist other Sheet");
            throw new ZtbWebException(ResponseEnum.PROJECT_DELETE_FAILD);
        }

        return deleteProjectSheetLogical(id);
    }

    @Override
    public void sendMailByMember(Long id) {
        ProjectSheetVO projectSheetVO = queryProjectSheetById(id);
        ProjectMemberFilterDTO projectMemberFilterDTO = new ProjectMemberFilterDTO();
        projectMemberFilterDTO.setProjectId(id);
        List<ProjectMemberVO> projectMemberVOS = projectMemberService.queryProjectMemberVOList(projectMemberFilterDTO);
        //发送邮件给项目成员
        if (CollectionUtil.isNotEmpty(projectMemberVOS)) {

            ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
            extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.SIX));
            extraMailNotificationInfoDTO.setRequestId(id);



            projectMemberVOS.forEach(projectMemberVO -> {
                MailBeanInfo mailBeanInfo = new MailBeanInfo();
                mailBeanInfo.setMail(projectMemberVO.getEmail());
                mailBeanInfo.setName(projectMemberVO.getName());

                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("projectCode", projectSheetVO.getCode());
                objectMap.put("projectName", projectSheetVO.getTitle());
                objectMap.put("name", projectMemberVO.getName());

                extraMailNotificationInfoDTO.setExtra(objectMap);
                extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
                ieService.emailNotification(extraMailNotificationInfoDTO);
            });
        }

    }

    private void sendMailProjectNumbers(List<ProjectMemberVO> projectMemberVOS, ProjectSheetVO projectSheetVO) {

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

    private void judgeTitle(String title){
        ProjectSheetFilterDTO projectSheetFilterDTO=new ProjectSheetFilterDTO();
        projectSheetFilterDTO.setTitle(title);
        List<ProjectSheetVO> projectSheetVOS = queryProjectSheetVOList(projectSheetFilterDTO);
        if(CollectionUtil.isNotEmpty(projectSheetVOS)){
            log.error("saveProjectSheetReturnVO() The title already exists");
            throw new ZtbWebException(ResponseEnum.PROJECT_TITLE_IS_EXIST);
        }
    }


}
