package com.xdcplus.vendor.controller;


import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.vendor.common.pojo.dto.ProjectSheetDTO;
import com.xdcplus.vendor.common.pojo.dto.ProjectSheetFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.vendor.common.pojo.vo.ProjectSheetVO;
import com.xdcplus.vendor.service.ProjectSheetService;
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
 * @since 2021-09-03 09:00:14
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

    @ApiOperation("新增项目")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO saveProjectSheet(@RequestBody ProjectSheetDTO projectSheetDTO) {

        log.info("saveProjectSheet {}", projectSheetDTO.toString());

        projectSheetDTO.setCreatedUser(getAccount());
        projectSheetService.saveProjectSheet(projectSheetDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改项目")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateProjectSheet(@RequestBody ProjectSheetDTO projectSheetDTO) {

        log.info("updateProjectSheet {}", projectSheetDTO.toString());

        projectSheetDTO.setUpdatedUser(getAccount());
        projectSheetService.updateProjectSheet(projectSheetDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除项目")
    @DeleteMapping(value = "/{projectSheetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "projectSheetId", dataType = "Long", value = "项目ID", required = true),
    })
    public ResponseVO deleteProjectSheetLogical(@PathVariable("projectSheetId")
                                                @NotNull(message = "项目ID不能为空") Long projectSheetId) {

        log.info("deleteProjectSheetLogical {}", projectSheetId);

        projectSheetService.deleteProjectSheetLogical(projectSheetId);

        return ResponseVO.success();
    }

    @ApiOperation("查询项目")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<ProjectSheetVO>> findProjectSheet(ProjectSheetFilterDTO projectSheetFilterDTO) {

        log.info("findProjectSheet {}", projectSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(projectSheetFilterDTO);

        return ResponseVO.success(projectSheetService.queryProjectSheet(projectSheetFilterDTO));
    }


}
