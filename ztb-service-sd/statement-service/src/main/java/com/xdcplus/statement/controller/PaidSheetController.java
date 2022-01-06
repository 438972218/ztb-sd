package com.xdcplus.statement.controller;


import com.xdcplus.statement.common.pojo.vo.PaidSheetStatementVO;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.statement.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.statement.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.statement.common.pojo.vo.PaidSheetVO;
import com.xdcplus.statement.service.PaidSheetService;
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
 * 竞价单(PaidSheet)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:02:03
 */
@Api(tags = "竞价单(PaidSheet)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("paidSheet")
public class PaidSheetController extends AbstractController {

    @Autowired
    private PaidSheetService paidSheetService;

    @ApiOperation("查询竞价单(报表)")
    @GetMapping(value = "/statement", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<PaidSheetStatementVO>> findPaidSheetWithStatement(PaidSheetFilterDTO paidSheetFilterDTO) {

        log.info("findPaidSheetWithStatement {}", paidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidSheetFilterDTO);
        PageVO<PaidSheetStatementVO> paidSheetStatementVOPageVO = paidSheetService.queryPaidSheetWithStatement(paidSheetFilterDTO);

        return ResponseVO.success(paidSheetStatementVOPageVO);
    }

}
