package com.xdcplus.vendor.service.impl;

import com.xdcplus.vendor.generator.impl.ProjectSheetBaseServiceImpl;
import com.xdcplus.vendor.mapper.ProjectSheetMapper;
import com.xdcplus.vendor.common.pojo.entity.ProjectSheet;
import com.xdcplus.vendor.common.pojo.vo.ProjectSheetVO;
import com.xdcplus.vendor.service.ProjectSheetService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目(ProjectSheet)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-03 09:00:14
 */
@Slf4j
@Service("projectSheetService")
public class ProjectSheetServiceImpl extends ProjectSheetBaseServiceImpl<ProjectSheet, ProjectSheetVO, ProjectSheet, ProjectSheetMapper> implements ProjectSheetService {


}
