package com.xdcplus.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.biz.common.pojo.dto.ProjectAttachmentFilterDTO;
import com.xdcplus.biz.generator.impl.ProjectAttachmentBaseServiceImpl;
import com.xdcplus.biz.mapper.ProjectAttachmentMapper;
import com.xdcplus.biz.common.pojo.entity.ProjectAttachment;
import com.xdcplus.biz.common.pojo.vo.ProjectAttachmentVO;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.service.ProjectAttachmentService;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 项目附件(ProjectAttachment)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-20 16:27:41
 */
@Slf4j
@Service("projectAttachmentService")
public class ProjectAttachmentServiceImpl extends ProjectAttachmentBaseServiceImpl<ProjectAttachment, ProjectAttachmentVO, ProjectAttachment, ProjectAttachmentMapper> implements ProjectAttachmentService {

    @Autowired
    PermService permService;

    @Override
    public PageVO<ProjectAttachmentVO> queryProjectAttachmentWithName(ProjectAttachmentFilterDTO projectAttachmentFilterDTO) {
        projectAttachmentFilterDTO.setDeleted(0);
        PageVO<ProjectAttachmentVO> pageVO = new PageVO<>();

        if (projectAttachmentFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(projectAttachmentFilterDTO.getCurrentPage(), projectAttachmentFilterDTO.getPageSize(),
                    projectAttachmentFilterDTO.getOrderType(), projectAttachmentFilterDTO.getOrderField());
        }

        List<ProjectAttachment> projectAttachmentList = queryProjectAttachmentList(projectAttachmentFilterDTO);

        PageInfo<ProjectAttachment> pageInfo = new PageInfo<>(projectAttachmentList);
        List<ProjectAttachmentVO> projectAttachmentVOS = this.objectConversion(projectAttachmentList);
        if(CollectionUtil.isEmpty(projectAttachmentVOS)){
            return pageVO;
        }
        projectAttachmentVOS.forEach(projectAttachmentVO ->
            projectAttachmentVO.setSysUserInfoVO(permService.queryByUserName(projectAttachmentVO.getCreatedUser()))
        );

        PropertyUtils.copyProperties(pageInfo, pageVO, projectAttachmentVOS);

        return pageVO;
    }
}
