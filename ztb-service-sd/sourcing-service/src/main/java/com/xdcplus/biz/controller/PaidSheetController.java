package com.xdcplus.biz.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.validation.ValidationUtil;
import com.alibaba.fastjson.JSONObject;
import com.xdcplus.biz.common.config.AssemblyBuilder;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.PaidVendorDTO;
import com.xdcplus.biz.common.pojo.dto.PaidVendorFilterDTO;
import com.xdcplus.biz.common.pojo.vo.PaidVendorVO;
import com.xdcplus.biz.remote.service.IeService;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.remote.service.VendorFeignService;
import com.xdcplus.biz.service.PaidVendorService;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.remote.domain.sourcing.BidRankingMessageInfo;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.biz.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.biz.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.biz.common.pojo.vo.PaidSheetVO;
import com.xdcplus.biz.service.PaidSheetService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;


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

    @ApiOperation("新增竞价单")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO savePaidSheet(@RequestBody PaidSheetDTO paidSheetDTO) {

        log.info("savePaidSheet {}", paidSheetDTO.toString());

        paidSheetDTO.setCreatedUser(getAccount());
        paidSheetService.savePaidSheet(paidSheetDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改竞价单")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updatePaidSheet(@RequestBody PaidSheetDTO paidSheetDTO) {

        log.info("updatePaidSheet {}", paidSheetDTO.toString());

        paidSheetDTO.setUpdatedUser(getAccount());
        paidSheetService.updatePaidSheet(paidSheetDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除竞价单")
    @DeleteMapping(value = "/{paidSheetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "paidSheetId", dataType = "Long", value = "竞价单ID", required = true),
    })
    public ResponseVO deletePaidSheetLogical(@PathVariable("paidSheetId")
                                             @NotNull(message = "竞价单ID不能为空") Long paidSheetId) {

        log.info("deletePaidSheetLogical {}", paidSheetId);

        paidSheetService.deletePaidSheetLogical(paidSheetId);

        return ResponseVO.success();
    }

    @ApiOperation("查询竞价单")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<PaidSheetVO>> findPaidSheet(PaidSheetFilterDTO paidSheetFilterDTO) {

        log.info("findPaidSheet {}", paidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidSheetFilterDTO);

        return ResponseVO.success(paidSheetService.queryPaidSheet(paidSheetFilterDTO));
    }


    @ApiOperation("查询竞价单模板")
    @GetMapping(value = "/template", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<PaidSheetVO>> findPaidSheetTemplate(PaidSheetFilterDTO paidSheetFilterDTO) {

        log.info("findPaidSheetTemplate {}", paidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidSheetFilterDTO);

        return ResponseVO.success(paidSheetService.queryPaidSheetTemplate(paidSheetFilterDTO));
    }


    @ApiOperation("show竞价单ByRequestId")
    @GetMapping(value = "showByRequestId/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "requestId", dataType = "Long", value = "requestID", required = true),
    })
    public ResponseVO<PaidSheetVO> showPaidInvitationByRequestId(@PathVariable("requestId")
                                                    @NotNull(message = "requestID不能为空") Long requestId) {

        log.info("showPaidInvitationByRequestId {}", requestId);

        PaidSheetVO paidSheetVO = paidSheetService.showPaidSheetByRequestId(requestId);

        return ResponseVO.success(paidSheetVO);
    }

    @ApiOperation("查询竞价单(user)")
    @GetMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<PaidSheetVO>> findPaidSheetWithRequest(PaidSheetFilterDTO paidSheetFilterDTO) {

        log.info("findPaidSheetByUser {}", paidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidSheetFilterDTO);
        PageVO<PaidSheetVO> paidSheetVOPageVO = paidSheetService.queryPaidSheetWithRequest1(paidSheetFilterDTO);

        return ResponseVO.success(paidSheetVOPageVO);
    }

    @ApiOperation("提交审批竞价单")
    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PaidSheetVO> submitPaidSheet(@RequestBody PaidSheetDTO paidSheetDTO) {

        log.info("submitPaidSheet {}", paidSheetDTO.toString());

        paidSheetDTO.setCreatedUser(getAccount());

        return ResponseVO.success(paidSheetService.submitPaidSheet(paidSheetDTO));
    }

    @ApiOperation("流转竞价单")
    @PostMapping(value = "/agree", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PaidSheetVO> agreePaidSheet(@RequestBody RequestFlowDTO requestFlowDTO) {

        log.info("agreePaidSheet {}", requestFlowDTO.toString());

        return ResponseVO.success(paidSheetService.agreePaidSheet(requestFlowDTO));
    }

    @ApiOperation("修改竞价单返回VO")
    @PutMapping(value = "/returnVO", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PaidSheetVO> updatePaidSheetReturnVO(@RequestBody PaidSheetDTO paidSheetDTO) {

        log.info("updatePaidSheetReturnVO {}", paidSheetDTO.toString());

        paidSheetDTO.setUpdatedUser(getAccount());
        paidSheetService.updatePaidSheet(paidSheetDTO);

        return ResponseVO.success(paidSheetService.queryPaidSheetWithProjectById(paidSheetDTO.getId()));
    }

    @ApiOperation("暂停竞价单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "sheetId", dataType = "Long", value = "sheetId", required = true),
    })
    @GetMapping(value = "pause/{sheetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PaidSheetVO> pausePaidSheet(@PathVariable("sheetId")
                                     @NotNull(message = "sheetId不能为空") Long sheetId) {

        log.info("pausePaidSheet {}", sheetId.toString());

        paidSheetService.pausePaidSheet(sheetId);

        return ResponseVO.success(paidSheetService.showPaidSheetById(sheetId));
    }

    @ApiOperation("恢复竞价单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "sheetId", dataType = "Long", value = "sheetId", required = true),
    })
    @GetMapping(value = "recover/{sheetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PaidSheetVO> recoverPaidSheet(@PathVariable("sheetId")
                                       @NotNull(message = "sheetId不能为空") Long sheetId) {

        log.info("recoverPaidSheet {}", sheetId.toString());

        paidSheetService.recoverPaidSheet(sheetId);

        return ResponseVO.success(paidSheetService.showPaidSheetById(sheetId));
    }

    @ApiOperation("重新竞价竞价单")
    @PostMapping(value = "anew", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PaidSheetVO> anewPaidSheet(@RequestBody PaidSheetDTO paidSheetDTO) {

        log.info("anewPaidSheet {}", paidSheetDTO.toString());

        paidSheetService.anewPaidSheet(paidSheetDTO);

        return ResponseVO.success(paidSheetService.showPaidSheetById(paidSheetDTO.getId()));
    }

    @ApiOperation("终止竞价单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "sheetId", dataType = "Long", value = "sheetId", required = true),
    })
    @GetMapping(value = "terminate/{sheetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PaidSheetVO> terminatePaidSheet(@PathVariable("sheetId")
                                         @NotNull(message = "sheetId不能为空") Long sheetId) {

        log.info("recoverPaidSheet {}", sheetId.toString());

        paidSheetService.terminatePaidSheet(sheetId);

        return ResponseVO.success(paidSheetService.showPaidSheetById(sheetId));
    }


}
