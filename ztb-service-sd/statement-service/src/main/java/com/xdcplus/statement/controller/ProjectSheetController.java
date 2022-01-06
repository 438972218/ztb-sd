package com.xdcplus.statement.controller;


import com.xdcplus.statement.service.ProjectSheetService;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.statement.common.pojo.dto.ProjectSheetDTO;
import com.xdcplus.statement.common.pojo.dto.ProjectSheetFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.statement.common.pojo.vo.ProjectSheetVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validation;
import javax.validation.constraints.NotNull;


/**
 * 项目(ProjectSheet)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-08-20 16:31:02
 */
@Api(tags = "项目(ProjectSheet)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("projectSheet")
public class ProjectSheetController extends AbstractController {

    @Autowired
    private ProjectSheetService projectSheetService;

    @ApiOperation("查询项目(用户)")
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<ProjectSheetVO>> findProjectSheetWithUser(ProjectSheetFilterDTO projectSheetFilterDTO) {

        log.info("findProjectSheetWithUser {}", projectSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(projectSheetFilterDTO);
        PageVO<ProjectSheetVO> projectSheetVOPageVO = projectSheetService.queryProjectSheetWithUser1(projectSheetFilterDTO);

        return ResponseVO.success(projectSheetVOPageVO);
    }

}
