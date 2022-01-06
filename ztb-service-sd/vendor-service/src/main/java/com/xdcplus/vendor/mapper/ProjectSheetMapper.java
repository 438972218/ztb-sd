package com.xdcplus.vendor.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.vendor.common.pojo.entity.ProjectSheet;
import com.xdcplus.vendor.common.pojo.query.ProjectSheetQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目(ProjectSheet)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-10 13:59:02
 */
public interface ProjectSheetMapper extends IBaseMapper<ProjectSheet> {

    /**
     * 查询项目(ProjectSheet)
     *
     * @param projectSheetQuery 项目(ProjectSheet)查询
     * @return {@link List<ProjectSheet>}
     */
    List<ProjectSheet> queryProjectSheet(ProjectSheetQuery projectSheetQuery);

}
