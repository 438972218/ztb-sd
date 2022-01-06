package com.xdcplus.biz.controller;


import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.biz.common.pojo.dto.OperationLogDTO;
import com.xdcplus.biz.common.pojo.dto.OperationLogFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.biz.common.pojo.vo.OperationLogVO;
import com.xdcplus.biz.service.OperationLogService;
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
 * (OperationLog)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-10-13 14:22:42
 */
@Api(tags = "(OperationLog)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("operationLog")
public class OperationLogController extends AbstractController {

    @Autowired
    private OperationLogService operationLogService;

    @ApiOperation("新增")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO saveOperationLog(@RequestBody OperationLogDTO operationLogDTO) {

        log.info("saveOperationLog {}", operationLogDTO.toString());

        operationLogDTO.setCreatedUser(getAccount());
        operationLogService.saveOperationLog(operationLogDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateOperationLog(@RequestBody OperationLogDTO operationLogDTO) {

        log.info("updateOperationLog {}", operationLogDTO.toString());

        operationLogDTO.setUpdatedUser(getAccount());
        operationLogService.updateOperationLog(operationLogDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除")
    @DeleteMapping(value = "/{operationLogId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "operationLogId", dataType = "Long", value = "ID", required = true),
    })
    public ResponseVO deleteOperationLogLogical(@PathVariable("operationLogId")
                                                @NotNull(message = "ID不能为空") Long operationLogId) {

        log.info("deleteOperationLogLogical {}", operationLogId);

        operationLogService.deleteOperationLogLogical(operationLogId);

        return ResponseVO.success();
    }

    @ApiOperation("查询")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<OperationLogVO>> findOperationLog(OperationLogFilterDTO operationLogFilterDTO) {

        log.info("findOperationLog {}", operationLogFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(operationLogFilterDTO);

        return ResponseVO.success(operationLogService.queryOperationLog(operationLogFilterDTO));
    }


}
