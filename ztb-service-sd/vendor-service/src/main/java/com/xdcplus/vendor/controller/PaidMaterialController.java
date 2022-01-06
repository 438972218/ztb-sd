package com.xdcplus.vendor.controller;


import cn.hutool.extra.validation.ValidationUtil;
import com.xdcplus.vendor.websocket.BidRankingMessage;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.vendor.service.PaidMaterialService;
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
import java.util.List;


/**
 * 竞价物品(PaidMaterial)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-09-03 09:01:57
 */
@Api(tags = "竞价物品(PaidMaterial)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("paidMaterial")
public class PaidMaterialController extends AbstractController {

    @Autowired
    private PaidMaterialService paidMaterialService;

    @ApiOperation("新增竞价物品")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO savePaidMaterial(@RequestBody PaidMaterialDTO paidMaterialDTO) {

        log.info("savePaidMaterial {}", paidMaterialDTO.toString());

        paidMaterialDTO.setCreatedUser(getAccount());
        paidMaterialService.savePaidMaterial(paidMaterialDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改竞价物品")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updatePaidMaterial(@RequestBody PaidMaterialDTO paidMaterialDTO) {

        log.info("updatePaidMaterial {}", paidMaterialDTO.toString());

        paidMaterialDTO.setUpdatedUser(getAccount());
        paidMaterialService.updatePaidMaterial(paidMaterialDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除竞价物品")
    @DeleteMapping(value = "/{paidMaterialId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "paidMaterialId", dataType = "Long", value = "竞价物品ID", required = true),
    })
    public ResponseVO deletePaidMaterialLogical(@PathVariable("paidMaterialId")
                                                @NotNull(message = "竞价物品ID不能为空") Long paidMaterialId) {

        log.info("deletePaidMaterialLogical {}", paidMaterialId);

        paidMaterialService.deletePaidMaterialLogical(paidMaterialId);

        return ResponseVO.success();
    }

    @ApiOperation("查询竞价物品")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<PaidMaterialVO>> findPaidMaterial(PaidMaterialFilterDTO paidMaterialFilterDTO) {

        log.info("findPaidMaterial {}", paidMaterialFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidMaterialFilterDTO);

        return ResponseVO.success(paidMaterialService.queryPaidMaterial(paidMaterialFilterDTO));
    }

    @ApiOperation("查询竞价物品WithChild")
    @GetMapping(value = "/{paidSheetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "paidSheetId", dataType = "Long", value = "竞价单ID", required = true),
    })
    public ResponseVO<List<PaidMaterialVO>> queryPaidMaterialVOBySheetId(@PathVariable("paidSheetId")
                                                                         @NotNull(message = "竞价单ID不能为空") Long paidSheetId) {

        log.info("queryPaidMaterialVOBySheetId {}", paidSheetId);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidSheetId);

        return ResponseVO.success(paidMaterialService.queryPaidMaterialVOBySheetId(paidSheetId));
    }

    @ApiOperation("查询出价排名")
    @GetMapping(value = "/lastRanking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<BidRankingMessage> getLatestRankingByRequestId(Long requestId) {

        log.info("getLatestRankingByRequestId {}", requestId.toString());

        ValidationUtil.validate(requestId);

        BidRankingMessage bidRankingMessage = paidMaterialService.getRankingByRequestId(requestId);

        return ResponseVO.success(bidRankingMessage);
    }

    @ApiOperation("根据供应商用户ID查询出价排名")
    @GetMapping(value = "/lastRankingByVendor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<BidRankingMessage> getRankingByVendorUserIdAndRequestId(@RequestParam("vendorUserId")Long vendorUserId,
                                                                              @RequestParam("requestId")Long requestId) {

        log.info("getRankingByVendorUserIdAndRequestId {}", requestId.toString());

        ValidationUtil.validate(requestId);

        BidRankingMessage bidRankingMessage = paidMaterialService.getRankingByVendorUserIdAndRequestId(vendorUserId,requestId);

        return ResponseVO.success(bidRankingMessage);
    }

    @ApiOperation("根据供应商表单ID和物品ID查询出价排名")
    @GetMapping(value = "/lastRankingByRequestAndMaterial", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<BidRankingMessage> getRankingByRequestIdAndMaterialId(@RequestParam("materialId")Long materialId,
                                                                              @RequestParam("requestId") Long requestId) {

        log.info("getRankingByRequestIdAndMaterialId {}", requestId.toString());

        ValidationUtil.validate(requestId);

        BidRankingMessage bidRankingMessage = paidMaterialService.getRankingByRequestIdAndMaterialId(requestId, materialId);

        return ResponseVO.success(bidRankingMessage);
    }


    @ApiOperation("根据表单id和userId查询授权的品项")
    @GetMapping(value = "/mandateByVendor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<List<PaidMaterialVO>> getMandateByRequestIdAndVendorUserId(@RequestParam("vendorUserId")Long vendorUserId,
                                                                              @RequestParam("requestId")Long requestId) {

        log.info("getMandateByRequestIdAndVendorUserId {}", requestId.toString());

        ValidationUtil.validate(requestId);

        List<PaidMaterialVO> paidMaterialVOS = paidMaterialService.getMandateByRequestIdAndVendorUserId(requestId,vendorUserId);

        return ResponseVO.success(paidMaterialVOS);
    }


}
