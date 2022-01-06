package com.xdcplus.vendor.controller;


import cn.hutool.extra.validation.ValidationUtil;
import com.xdcplus.vendor.common.pojo.dto.OfferFilterDTO;
import com.xdcplus.vendor.common.pojo.vo.OfferVO;
import com.xdcplus.vendor.service.OfferService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 出价记录 前端控制器
 *
 * @author Rong.Jia
 * @since 2021-08-17
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @ApiOperation("查询出价记录")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<OfferVO>> findOffers(OfferFilterDTO offerFilterDTO) {

        log.info("findOffers {}", offerFilterDTO.toString());

        ValidationUtil.validate(offerFilterDTO);

        PageVO<OfferVO> pageVO = offerService.findOffer(offerFilterDTO);

        return ResponseVO.success(pageVO);
    }

    @ApiOperation("根据表单ID删除出价记录")
    @DeleteMapping(value = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "requestId", dataType = "Long", value = "表单ID", required = true),
    })
    public ResponseVO<Boolean> deleteOfferByRequestId(@PathVariable("requestId")
                                             @NotNull(message = "表单ID不能为空") Long requestId) {

        log.info("deleteOfferByRequestId {}", requestId);

        return ResponseVO.success(offerService.deleteOfferByRequestId(requestId));
    }
















}
