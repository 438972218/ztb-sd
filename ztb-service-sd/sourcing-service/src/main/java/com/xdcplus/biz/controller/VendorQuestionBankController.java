package com.xdcplus.biz.controller;


import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.biz.common.pojo.dto.VendorQuestionBankDTO;
import com.xdcplus.biz.common.pojo.dto.VendorQuestionBankFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.biz.common.pojo.vo.VendorQuestionBankVO;
import com.xdcplus.biz.service.VendorQuestionBankService;
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
 * 模板题库表(VendorQuestionBank)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:29
 */
@Api(tags = "模板题库表(VendorQuestionBank)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("vendorQuestionBank")
public class VendorQuestionBankController extends AbstractController {

    @Autowired
    private VendorQuestionBankService vendorQuestionBankService;

    @ApiOperation("新增模板题库表")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO saveVendorQuestionBank(@RequestBody VendorQuestionBankDTO vendorQuestionBankDTO) {

        log.info("saveVendorQuestionBank {}", vendorQuestionBankDTO.toString());

        vendorQuestionBankDTO.setCreatedUser(getAccount());
        vendorQuestionBankService.saveVendorQuestionBank(vendorQuestionBankDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改模板题库表")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updateVendorQuestionBank(@RequestBody VendorQuestionBankDTO vendorQuestionBankDTO) {

        log.info("updateVendorQuestionBank {}", vendorQuestionBankDTO.toString());

        vendorQuestionBankDTO.setUpdatedUser(getAccount());
        vendorQuestionBankService.updateVendorQuestionBank(vendorQuestionBankDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除模板题库表")
    @DeleteMapping(value = "/{vendorQuestionBankId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "vendorQuestionBankId", dataType = "Long", value = "模板题库表ID", required = true),
    })
    public ResponseVO deleteVendorQuestionBankLogical(@PathVariable("vendorQuestionBankId")
                                                      @NotNull(message = "模板题库表ID不能为空") Long vendorQuestionBankId) {

        log.info("deleteVendorQuestionBankLogical {}", vendorQuestionBankId);

        vendorQuestionBankService.deleteVendorQuestionBankLogical(vendorQuestionBankId);

        return ResponseVO.success();
    }

    @ApiOperation("查询模板题库表")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<VendorQuestionBankVO>> findVendorQuestionBank(VendorQuestionBankFilterDTO vendorQuestionBankFilterDTO) {

        log.info("findVendorQuestionBank {}", vendorQuestionBankFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(vendorQuestionBankFilterDTO);

        return ResponseVO.success(vendorQuestionBankService.queryVendorQuestionBank(vendorQuestionBankFilterDTO));
    }


    @ApiOperation("查询模板题库表WithChild")
    @GetMapping(value = "/queryWithChild", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<List<VendorQuestionBankVO>> queryWithChild(VendorQuestionBankFilterDTO vendorQuestionBankFilterDTO) {

        log.info("queryWithChild {}", vendorQuestionBankFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(vendorQuestionBankFilterDTO);

        return ResponseVO.success(vendorQuestionBankService.queryWithChild(vendorQuestionBankFilterDTO));
    }


}
