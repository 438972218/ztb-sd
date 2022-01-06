package com.xdcplus.biz.controller;


import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiDetailDTO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiDetailFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.biz.common.pojo.vo.VendorKpiDetailVO;
import com.xdcplus.biz.service.VendorKpiDetailService;
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
 * 供应商绩效考核表明细(VendorKpiDetail)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:21
 */
@Api(tags = "供应商绩效考核表明细(VendorKpiDetail)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("vendorKpiDetail")
public class VendorKpiDetailController extends AbstractController {

    @Autowired
    private VendorKpiDetailService vendorKpiDetailService;

    @ApiOperation("新增供应商绩效考核表明细")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO saveVendorKpiDetail(@RequestBody VendorKpiDetailDTO vendorKpiDetailDTO) {

        log.info("saveVendorKpiDetail {}", vendorKpiDetailDTO.toString());

        vendorKpiDetailDTO.setCreatedUser(getAccount());
        vendorKpiDetailService.saveVendorKpiDetail(vendorKpiDetailDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改供应商绩效考核表明细")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateVendorKpiDetail(@RequestBody VendorKpiDetailDTO vendorKpiDetailDTO) {

        log.info("updateVendorKpiDetail {}", vendorKpiDetailDTO.toString());

        vendorKpiDetailDTO.setUpdatedUser(getAccount());
        vendorKpiDetailService.updateVendorKpiDetail(vendorKpiDetailDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除供应商绩效考核表明细")
    @DeleteMapping(value = "/{vendorKpiDetailId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "vendorKpiDetailId", dataType = "Long", value = "供应商绩效考核表明细ID", required = true),
    })
    public ResponseVO deleteVendorKpiDetailLogical(@PathVariable("vendorKpiDetailId")
                                                   @NotNull(message = "供应商绩效考核表明细ID不能为空") Long vendorKpiDetailId) {

        log.info("deleteVendorKpiDetailLogical {}", vendorKpiDetailId);

        vendorKpiDetailService.deleteVendorKpiDetailLogical(vendorKpiDetailId);

        return ResponseVO.success();
    }

    @ApiOperation("查询供应商绩效考核表明细")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<VendorKpiDetailVO>> findVendorKpiDetail(VendorKpiDetailFilterDTO vendorKpiDetailFilterDTO) {

        log.info("findVendorKpiDetail {}", vendorKpiDetailFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(vendorKpiDetailFilterDTO);

        return ResponseVO.success(vendorKpiDetailService.queryVendorKpiDetail(vendorKpiDetailFilterDTO));
    }


}
