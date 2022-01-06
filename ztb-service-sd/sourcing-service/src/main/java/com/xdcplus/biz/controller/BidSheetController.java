package com.xdcplus.biz.controller;


import com.xdcplus.biz.common.pojo.dto.*;
import com.xdcplus.biz.remote.service.IeService;
import com.xdcplus.biz.service.*;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.tool.anotation.SystemControllerLog;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.biz.common.pojo.vo.BidSheetVO;
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

    @Autowired
    private BidVendorService bidVendorService;

    @Autowired
    private IeService ieService;

    @ApiOperation("新增bid招标单")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO saveBidSheet(@RequestBody BidSheetDTO bidSheetDTO) {

        log.info("saveBidSheet {}", bidSheetDTO.toString());

        bidSheetDTO.setCreatedUser(getAccount());
        bidSheetService.saveBidSheet(bidSheetDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改bid招标单")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateBidSheet(@RequestBody BidSheetDTO bidSheetDTO) {

        log.info("updateBidSheet {}", bidSheetDTO.toString());

        bidSheetDTO.setUpdatedUser(getAccount());
        bidSheetService.updateBidSheet(bidSheetDTO);

        return ResponseVO.success();
    }


    @ApiOperation("删除bid招标单")
    @DeleteMapping(value = "/{bidSheetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "bidSheetId", dataType = "Long", value = "bid招标单ID", required = true),
    })
    public ResponseVO deleteBidSheetLogical(@PathVariable("bidSheetId")
                                            @NotNull(message = "bid招标单ID不能为空") Long bidSheetId) {

        log.info("deleteBidSheetLogical {}", bidSheetId);

        bidSheetService.deleteBidSheetLogical(bidSheetId);

        return ResponseVO.success();
    }

    @ApiOperation("查询bid招标单")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<BidSheetVO>> findBidSheet(BidSheetFilterDTO bidSheetFilterDTO) {

        log.info("findBidSheet {}", bidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(bidSheetFilterDTO);

        return ResponseVO.success(bidSheetService.queryBidSheet(bidSheetFilterDTO));
    }


    @ApiOperation("showbid招标单ByRequestId")
    @GetMapping(value = "showByRequestId/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "requestId", dataType = "Long", value = "requestID", required = true),
    })
    public ResponseVO showBidSheetByRequestId(@PathVariable("requestId")
                                              @NotNull(message = "requestID不能为空") Long requestId) {

        log.info("showBidSheetByRequestId {}", requestId);

        BidSheetVO bidSheetVO = bidSheetService.showBidSheetByRequestId(requestId);

        return ResponseVO.success(bidSheetVO);
    }

//    @ApiOperation("查询招标单(用户)")
//    @GetMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseVO<PageVO<BidSheetVO>> findBidSheetWithRequest(BidSheetFilterDTO bidSheetFilterDTO) {
//
//        log.info("findBidSheetWithRequest {}", bidSheetFilterDTO);
//
//        Validation.buildDefaultValidatorFactory().getValidator().validate(bidSheetFilterDTO);
//        PageVO<BidSheetVO> bidSheetVOPageVO = bidSheetService.queryBidSheetWithRequest(bidSheetFilterDTO);
//
//        return ResponseVO.success(bidSheetVOPageVO);
//    }

    @ApiOperation("查询招标单(用户)")
    @GetMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<BidSheetVO>> findBidSheetWithRequest(BidSheetFilterDTO bidSheetFilterDTO) {

        log.info("findBidSheetWithRequest {}", bidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(bidSheetFilterDTO);
        PageVO<BidSheetVO> bidSheetVOPageVO = bidSheetService.queryBidSheetWithRequest4(bidSheetFilterDTO);

        return ResponseVO.success(bidSheetVOPageVO);
    }

    @ApiOperation("查询bid招标单模板")
    @GetMapping(value = "/template", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<BidSheetVO>> findBidSheetTemplate(BidSheetFilterDTO bidSheetFilterDTO) {

        log.info("findBidSheetTemplate {}", bidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(bidSheetFilterDTO);

        return ResponseVO.success(bidSheetService.queryBidSheetTemplate(bidSheetFilterDTO));
    }

    @SystemControllerLog(description="提交招标单")
    @ApiOperation("提交审批招标单")
    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<BidSheetVO> submitBidSheet(@RequestBody BidSheetDTO bidSheetDTO) {

        log.info("submitBidSheet {}", bidSheetDTO.toString());
        bidSheetDTO.setCreatedUser(getAccount());

        return ResponseVO.success(bidSheetService.submitBidSheet(bidSheetDTO));
    }

    @SystemControllerLog(description="流转招标单")
    @ApiOperation("流转招标单")
    @PostMapping(value = "/agree", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<BidSheetVO> agreeBidSheet(@RequestBody RequestFlowDTO requestFlowDTO) {

        log.info("agreeBidSheet {}", requestFlowDTO.toString());

        return ResponseVO.success(bidSheetService.agreeBidSheet(requestFlowDTO));
    }

    @SystemControllerLog(description="退回招标单")
    @ApiOperation("退回招标单")
    @PostMapping(value = "/back", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<BidSheetVO> backBidSheet(@RequestBody BidSheetDTO bidSheetDTO) {

        log.info("backBidSheet {}", bidSheetDTO.toString());

        return ResponseVO.success(bidSheetService.backBidSheet(bidSheetDTO));
    }

    @SystemControllerLog(description="修改招标单")
    @ApiOperation("修改bid招标单返回VO")
    @PutMapping(value = "/returnVO", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateBidSheetReturnVO(@RequestBody BidSheetDTO bidSheetDTO) {

        log.info("updateBidSheetReturnVO {}", bidSheetDTO.toString());

        bidSheetDTO.setUpdatedUser(getAccount());
        bidSheetService.updateBidSheet(bidSheetDTO);

        return ResponseVO.success(bidSheetService.showBidSheetByRequestId(bidSheetDTO.getRequestId()));
    }

}
