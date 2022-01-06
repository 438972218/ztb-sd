package com.xdcplus.biz.controller;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.xdcplus.biz.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.biz.common.pojo.vo.PaidSheetVO;
import com.xdcplus.biz.remote.service.IeService;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.service.VendorService;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiDTO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.biz.common.pojo.vo.VendorKpiVO;
import com.xdcplus.biz.service.VendorKpiService;
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

import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 供应商绩效考核表(VendorKpi)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:18
 */
@Api(tags = "供应商绩效考核表(VendorKpi)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("vendorKpi")
public class VendorKpiController extends AbstractController {

    @Autowired
    private VendorKpiService vendorKpiService;

    @Autowired
    private IeService ieService;

    @Autowired
    private PermService permService;

    @Autowired
    private VendorService vendorService;

    @ApiOperation("新增供应商绩效考核表")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO saveVendorKpi(@RequestBody VendorKpiDTO vendorKpiDTO) {

        log.info("saveVendorKpi {}", vendorKpiDTO.toString());

        vendorKpiDTO.setCreatedUser(getAccount());
        vendorKpiService.saveVendorKpi(vendorKpiDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改供应商绩效考核表")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateVendorKpi(@RequestBody VendorKpiDTO vendorKpiDTO) {

        log.info("updateVendorKpi {}", vendorKpiDTO.toString());

        vendorKpiDTO.setUpdatedUser(getAccount());
        vendorKpiService.updateVendorKpi(vendorKpiDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除供应商绩效考核表")
    @DeleteMapping(value = "/{vendorKpiId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "vendorKpiId", dataType = "Long", value = "供应商绩效考核表ID", required = true),
    })
    public ResponseVO deleteVendorKpiLogical(@PathVariable("vendorKpiId")
                                             @NotNull(message = "供应商绩效考核表ID不能为空") Long vendorKpiId) {

        log.info("deleteVendorKpiLogical {}", vendorKpiId);

        vendorKpiService.deleteVendorKpiLogical(vendorKpiId);

        return ResponseVO.success();
    }

    @ApiOperation("查询供应商绩效考核表")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<VendorKpiVO>> findVendorKpi(VendorKpiFilterDTO vendorKpiFilterDTO) {

        log.info("findVendorKpi {}", vendorKpiFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(vendorKpiFilterDTO);

        return ResponseVO.success(vendorKpiService.queryVendorKpi(vendorKpiFilterDTO));
    }


    @ApiOperation("查询供应商绩效(user)")
    @GetMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<VendorKpiVO>> findVendorKpiWithRequest(VendorKpiFilterDTO vendorKpiFilterDTO) {

        log.info("findVendorKpiWithRequest {}", vendorKpiFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(vendorKpiFilterDTO);
        PageVO<VendorKpiVO> vendorKpiVOPageVO = vendorKpiService.queryVendorKpiWithRequest(vendorKpiFilterDTO);

        return ResponseVO.success(vendorKpiVOPageVO);
    }

    @ApiOperation("show供应商绩效考核表")
    @GetMapping(value = "/show/{vendorKpiId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "vendorKpiId", dataType = "Long", value = "供应商绩效考核表", required = true),
    })

    public ResponseVO<VendorKpiVO> showInquirySheetById(@PathVariable("vendorKpiId")
                                                        @NotNull(message = "电子调查表ID不能为空") Long vendorKpiId) {
        log.info("showVendorKpiById {}", vendorKpiId);
        return ResponseVO.success(vendorKpiService.showVendorKpiById(vendorKpiId));
    }

    @ApiOperation("修改供应商绩效考核表WithDetail")
    @PutMapping(value = "/withDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateVendorKpiWithDetail(@RequestBody @Valid VendorKpiDTO vendorKpiDTO) {

        log.info("updateVendorKpiWithDetail {}", vendorKpiDTO.toString());

        vendorKpiDTO.setUpdatedUser(getAccount());

        vendorKpiService.updateVendorKpiWithDetail(vendorKpiDTO);

        return ResponseVO.success();
    }


    @ApiOperation("提交绩效单")
    @PostMapping(value = "/submit", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO submitVendorKpi(@RequestBody VendorKpiDTO vendorKpiDTO) {

        log.info("submitVendorKpi {}", vendorKpiDTO.toString());
        vendorKpiDTO.setCreatedUser(getAccount());

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setRuleId(vendorKpiDTO.getRuleId());
        requestDTO.setTitle(vendorKpiDTO.getName() + "-" + DateUtil.formatDate(new Date()));

        requestDTO.setCreatedUser(vendorKpiDTO.getCreatedUser());

        submitBeforeJudgeStatus(vendorKpiDTO, requestDTO);
        RequestVO requestVO = ieService.saveRequest(requestDTO);

        vendorKpiDTO.setRequestId(requestVO.getId());
        VendorKpiVO vendorKpiVO = vendorKpiService.saveVendorKpiReturnVO(vendorKpiDTO);
        vendorKpiDTO.setId(vendorKpiVO.getId());
//        bidSheetService.submitAfterJudgeStatus(vendorKpiDTO);

        return ResponseVO.success(vendorKpiService.showVendorKpiById(vendorKpiVO.getId()));
    }

    @ApiOperation("流转绩效单")
    @PostMapping(value = "/agree", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<VendorKpiVO> agreeVendorKpi(@RequestBody RequestFlowDTO requestFlowDTO) {

        log.info("agreeVendorKpiDTO {}", requestFlowDTO.toString());

        ProcessTransforDTO processTransforDTO = approveJudgeStatus(requestFlowDTO);
        ResponseVO responseVO = ieService.processTransfor(processTransforDTO);
        approveAfterJudgeStatus(requestFlowDTO);
        return ResponseVO.success(vendorKpiService.showVendorKpiById(requestFlowDTO.getId()));
    }

    private void submitBeforeJudgeStatus(VendorKpiDTO vendorKpiDTO, RequestDTO requestDTO) {
        Circulation circulation = new Circulation();
        circulation.setUserId(vendorKpiDTO.getUserId());
        requestDTO.setCirculation(circulation);

        //新增策略条件
        FormFlowStrategyDTO formFlowStrategyDTO = new FormFlowStrategyDTO();
//        Object obj = JSONArray.toJSON(paidSheetDTO);
//        formFlowStrategyDTO.setStrategyConditions(obj);
        formFlowStrategyDTO.setTypeId(vendorKpiDTO.getTypeId());
        requestDTO.setStrategy(formFlowStrategyDTO);

        String json = JSONObject.toJSONString(vendorKpiDTO);
        Map map = JSONObject.parseObject(json, Map.class);
        formFlowStrategyDTO.setStrategyConditions(map);
    }

    private ProcessTransforDTO approveJudgeStatus(RequestFlowDTO requestFlowDTO) {
        Long requestId = requestFlowDTO.getRequestId();
        RequestVO requestVO = ieService.findRequestById(requestId);
        ProcessTransforDTO processTransforDTO = null;
        processTransforDTO = new ProcessTransforDTO();
        processTransforDTO.setFlowOption(1);
        processTransforDTO.setUserId(requestFlowDTO.getUserId());
        processTransforDTO.setRequestId(requestFlowDTO.getRequestId());
        processTransforDTO.setDescription(requestFlowDTO.getDescription());

        ProcessTransforDTO.Agree agree = new ProcessTransforDTO.Agree();
        List<Long> roleIds = permService.queryByUserId(requestFlowDTO.getUserId());
        agree.setRoleIds(roleIds);
        processTransforDTO.setAgree(agree);

        return processTransforDTO;
    }

    private void approveAfterJudgeStatus(RequestFlowDTO requestFlowDTO) {

    }


}
