package com.xdcplus.vendor.controller;


import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.vendor.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.vendor.common.pojo.vo.PaidSheetVO;
import com.xdcplus.vendor.service.PaidSheetService;
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
 * @since 2021-08-31 09:40:16
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



    @ApiOperation("查询竞价单(供应商)")
    @GetMapping(value = "/vendor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<PaidSheetVO>> findPaidSheetByVendor(PaidSheetFilterDTO paidSheetFilterDTO) {

        log.info("findPaidSheetByVendor {}", paidSheetFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidSheetFilterDTO);
        PageVO<PaidSheetVO> paidSheetVOPageVO = paidSheetService.queryPaidSheetByVendor(paidSheetFilterDTO);

        return ResponseVO.success(paidSheetVOPageVO);
    }

    @ApiOperation("show竞价单ByRequestId")
    @GetMapping(value = "showByRequestIdAndVendor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO showPaidInvitationByRequestId(@RequestParam("requestId")Long requestId,
                                                    @RequestParam("vendorUserId")Long vendorUserId) {

        log.info("showPaidInvitationByRequestId {}", requestId);

        PaidSheetVO paidSheetVO = paidSheetService.showPaidSheetByRequestIdAndVendor(requestId,vendorUserId);

        return ResponseVO.success(paidSheetVO);
    }

    @ApiOperation("根据requestId查询倒计时")
    @GetMapping(value = "countdown/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "requestId", dataType = "Long", value = "requestID", required = true),
    })
    public ResponseVO getCountdownByRequestId(@PathVariable("requestId")
                                                    @NotNull(message = "requestID不能为空") Long requestId) {

        log.info("getCountdownByRequestId {}", requestId);

        Long countdown = paidSheetService.getCountdownByRequestId(requestId);

        return ResponseVO.success(countdown);
    }

}
