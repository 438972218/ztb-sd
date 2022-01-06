package com.xdcplus.vendor.controller;


import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.vendor.common.pojo.dto.BidVendorAttachmentDTO;
import com.xdcplus.vendor.common.pojo.dto.BidVendorAttachmentFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.vendor.common.pojo.vo.BidVendorAttachmentVO;
import com.xdcplus.vendor.service.BidVendorAttachmentService;
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
 * 招标供应商附件(BidVendorAttachment)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-09-28 18:06:23
 */
@Api(tags = "招标供应商附件(BidVendorAttachment)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("bidVendorAttachment")
public class BidVendorAttachmentController extends AbstractController {

    @Autowired
    private BidVendorAttachmentService bidVendorAttachmentService;

    @ApiOperation("新增招标供应商附件")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO saveBidVendorAttachment(@RequestBody BidVendorAttachmentDTO bidVendorAttachmentDTO) {

        log.info("saveBidVendorAttachment {}", bidVendorAttachmentDTO.toString());

        bidVendorAttachmentDTO.setCreatedUser(getAccount());
        bidVendorAttachmentService.saveBidVendorAttachment(bidVendorAttachmentDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改招标供应商附件")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateBidVendorAttachment(@RequestBody BidVendorAttachmentDTO bidVendorAttachmentDTO) {

        log.info("updateBidVendorAttachment {}", bidVendorAttachmentDTO.toString());

        bidVendorAttachmentDTO.setUpdatedUser(getAccount());
        bidVendorAttachmentService.updateBidVendorAttachment(bidVendorAttachmentDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除招标供应商附件")
    @DeleteMapping(value = "/{bidVendorAttachmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "bidVendorAttachmentId", dataType = "Long", value = "招标供应商附件ID", required = true),
    })
    public ResponseVO deleteBidVendorAttachmentLogical(@PathVariable("bidVendorAttachmentId")
                                                       @NotNull(message = "招标供应商附件ID不能为空") Long bidVendorAttachmentId) {

        log.info("deleteBidVendorAttachmentLogical {}", bidVendorAttachmentId);

        bidVendorAttachmentService.deleteBidVendorAttachmentLogical(bidVendorAttachmentId);

        return ResponseVO.success();
    }

    @ApiOperation("查询招标供应商附件")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<BidVendorAttachmentVO>> findBidVendorAttachment(BidVendorAttachmentFilterDTO bidVendorAttachmentFilterDTO) {

        log.info("findBidVendorAttachment {}", bidVendorAttachmentFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(bidVendorAttachmentFilterDTO);

        return ResponseVO.success(bidVendorAttachmentService.queryBidVendorAttachment(bidVendorAttachmentFilterDTO));
    }

    @ApiOperation("查询招标供应商附件WithName")
    @GetMapping(value = "/withName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<BidVendorAttachmentVO>> findBidVendorAttachmentWithName(BidVendorAttachmentFilterDTO bidVendorAttachmentFilterDTO) {

        log.info("findBidVendorAttachmentWithName {}", bidVendorAttachmentFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(bidVendorAttachmentFilterDTO);

        return ResponseVO.success(bidVendorAttachmentService.queryBidVendorAttachmentWithName(bidVendorAttachmentFilterDTO));
    }


}
