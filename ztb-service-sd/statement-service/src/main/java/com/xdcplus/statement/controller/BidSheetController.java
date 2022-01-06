package com.xdcplus.statement.controller;


import com.xdcplus.statement.common.pojo.dto.BidSheetFilterDTO;
import com.xdcplus.statement.common.pojo.vo.BidSheetStatementVO;
import com.xdcplus.statement.service.BidSheetService;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validation;


/**
 * bid招标单(BidSheet)表服务控制层
 *
 * @author makejava
 * @since 2021-08-12 14:45:42
 */
@Api(tags = "bid招标单(BidSheet)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("bidSheet")
public class BidSheetController extends AbstractController {

    @Autowired
    private BidSheetService bidSheetService;

    @ApiOperation("查询招标单(报表)")
    @GetMapping(value = "/statement", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<BidSheetStatementVO>> findBidSheetWithStatement(BidSheetFilterDTO bidSheetFilterDTO) {

        log.info("findBidSheetWithStatement {}", bidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(bidSheetFilterDTO);
        PageVO<BidSheetStatementVO> bidSheetStatementVOPageVO = bidSheetService.queryBidSheetWithStatement(bidSheetFilterDTO);

        return ResponseVO.success(bidSheetStatementVOPageVO);
    }

}
