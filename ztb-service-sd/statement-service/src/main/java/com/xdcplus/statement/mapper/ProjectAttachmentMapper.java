package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.ProjectAttachment;
import com.xdcplus.statement.common.pojo.query.ProjectAttachmentQuery;

import java.util.List;

/**
 * 项目附件(ProjectAttachment)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-08-26 15:17:10
 */
public interface ProjectAttachmentMapper extends IBaseMapper<ProjectAttachment> {

    /**
     * 查询项目附件(ProjectAttachment)
     *
     * @param projectAttachmentQuery 项目附件(ProjectAttachment)查询
     * @return {@link List<ProjectAttachment>}
     */
    List<ProjectAttachment> queryProjectAttachment(ProjectAttachmentQuery projectAttachmentQuery);

}
