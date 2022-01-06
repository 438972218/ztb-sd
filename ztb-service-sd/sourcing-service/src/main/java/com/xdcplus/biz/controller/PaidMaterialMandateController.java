package com.xdcplus.biz.controller;


import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.biz.common.pojo.dto.PaidMaterialMandateDTO;
import com.xdcplus.biz.common.pojo.dto.PaidMaterialMandateFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.biz.common.pojo.vo.PaidMaterialMandateVO;
import com.xdcplus.biz.service.PaidMaterialMandateService;
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
 * 竞价品项授权记录(PaidMaterialMandate)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:45:32
 */
@Api(tags = "竞价品项授权记录(PaidMaterialMandate)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("paidMaterialMandate")
public class PaidMaterialMandateController extends AbstractController {

    @Autowired
    private PaidMaterialMandateService paidMaterialMandateService;

    @ApiOperation("新增竞价品项授权记录")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO savePaidMaterialMandate(@RequestBody PaidMaterialMandateDTO paidMaterialMandateDTO) {

        log.info("savePaidMaterialMandate {}", paidMaterialMandateDTO.toString());

        paidMaterialMandateDTO.setCreatedUser(getAccount());
        paidMaterialMandateService.savePaidMaterialMandate(paidMaterialMandateDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改竞价品项授权记录")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updatePaidMaterialMandate(@RequestBody PaidMaterialMandateDTO paidMaterialMandateDTO) {

        log.info("updatePaidMaterialMandate {}", paidMaterialMandateDTO.toString());

        paidMaterialMandateDTO.setUpdatedUser(getAccount());
        paidMaterialMandateService.updatePaidMaterialMandate(paidMaterialMandateDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除竞价品项授权记录")
    @DeleteMapping(value = "/{paidMaterialMandateId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "paidMaterialMandateId", dataType = "Long", value = "竞价品项授权记录ID", required = true),
    })
    public ResponseVO deletePaidMaterialMandateLogical(@PathVariable("paidMaterialMandateId")
                                                       @NotNull(message = "竞价品项授权记录ID不能为空") Long paidMaterialMandateId) {

        log.info("deletePaidMaterialMandateLogical {}", paidMaterialMandateId);

        paidMaterialMandateService.deletePaidMaterialMandateLogical(paidMaterialMandateId);

        return ResponseVO.success();
    }

    @ApiOperation("查询竞价品项授权记录")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<PaidMaterialMandateVO>> findPaidMaterialMandate(PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO) {

        log.info("findPaidMaterialMandate {}", paidMaterialMandateFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidMaterialMandateFilterDTO);

        return ResponseVO.success(paidMaterialMandateService.queryPaidMaterialMandate(paidMaterialMandateFilterDTO));
    }


}
