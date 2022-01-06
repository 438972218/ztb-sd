package com.xdcplus.statement.service;

import com.xdcplus.statement.common.pojo.dto.ProjectSheetDTO;
import com.xdcplus.statement.common.pojo.dto.ProjectSheetFilterDTO;
import com.xdcplus.statement.generator.ProjectSheetBaseService;
import com.xdcplus.statement.common.pojo.entity.ProjectSheet;
import com.xdcplus.statement.common.pojo.vo.ProjectSheetVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;


/**
 * 项目(ProjectSheet)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-20 16:31:01
 */
public interface ProjectSheetService extends ProjectSheetBaseService<ProjectSheet, ProjectSheetVO, ProjectSheet> {

    PageVO<ProjectSheetVO> queryProjectSheetWithUser(ProjectSheetFilterDTO projectSheetFilterDTO);

    PageVO<ProjectSheetVO> queryProjectSheetWithUser1(ProjectSheetFilterDTO projectSheetFilterDTO);

    PageVO<ProjectSheetVO> queryProjectSheetWithCreatedUser(ProjectSheetFilterDTO projectSheetFilterDTO);

}
