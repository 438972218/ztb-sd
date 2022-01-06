package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.ProjectSheet;
import com.xdcplus.statement.common.pojo.query.ProjectSheetQuery;

import java.util.List;

/**
 * 项目(ProjectSheet)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-10 13:55:02
 */
public interface ProjectSheetMapper extends IBaseMapper<ProjectSheet> {

    /**
     * 查询项目(ProjectSheet)
     *
     * @param projectSheetQuery 项目(ProjectSheet)查询
     * @return {@link List<ProjectSheet>}
     */
    List<ProjectSheet> queryProjectSheet(ProjectSheetQuery projectSheetQuery);

    List<ProjectSheet> queryProjectSheetWithUser(ProjectSheetQuery projectSheetQuery);

}
