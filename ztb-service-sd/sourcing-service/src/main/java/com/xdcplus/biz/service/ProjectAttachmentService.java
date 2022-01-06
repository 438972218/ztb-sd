package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.ProjectAttachmentFilterDTO;
import com.xdcplus.biz.generator.ProjectAttachmentBaseService;
import com.xdcplus.biz.common.pojo.entity.ProjectAttachment;
import com.xdcplus.biz.common.pojo.vo.ProjectAttachmentVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;


/**
 * 项目附件(ProjectAttachment)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-20 16:27:41
 */
public interface ProjectAttachmentService extends ProjectAttachmentBaseService<ProjectAttachment, ProjectAttachmentVO, ProjectAttachment> {

    PageVO<ProjectAttachmentVO> queryProjectAttachmentWithName(ProjectAttachmentFilterDTO projectAttachmentFilterDTO);


}
