package com.xdcplus.biz.controller;


import com.xdcplus.biz.common.pojo.vo.BidVendorVO;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.biz.common.pojo.dto.PaidVendorDTO;
import com.xdcplus.biz.common.pojo.dto.PaidVendorFilterDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.biz.common.pojo.vo.PaidVendorVO;
import com.xdcplus.biz.service.PaidVendorService;
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
 * 竞价供应商(PaidVendor)表服务控制层
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:02:06
 */
@Api(tags = "竞价供应商(PaidVendor)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("paidVendor")
public class PaidVendorController extends AbstractController {

    @Autowired
    private PaidVendorService paidVendorService;

    @ApiOperation("新增竞价供应商")
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO savePaidVendor(@RequestBody PaidVendorDTO paidVendorDTO) {

        log.info("savePaidVendor {}", paidVendorDTO.toString());

        paidVendorDTO.setCreatedUser(getAccount());
        paidVendorService.savePaidVendor(paidVendorDTO);

        return ResponseVO.success();
    }

    @ApiOperation("修改竞价供应商")
    @PutMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updatePaidVendor(@RequestBody PaidVendorDTO paidVendorDTO) {

        log.info("updatePaidVendor {}", paidVendorDTO.toString());

        paidVendorDTO.setUpdatedUser(getAccount());
        paidVendorService.updatePaidVendor(paidVendorDTO);

        return ResponseVO.success();
    }

    @ApiOperation("删除竞价供应商")
    @DeleteMapping(value = "/{paidVendorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "paidVendorId", dataType = "Long", value = "竞价供应商ID", required = true),
    })
    public ResponseVO deletePaidVendorLogical(@PathVariable("paidVendorId")
                                              @NotNull(message = "竞价供应商ID不能为空") Long paidVendorId) {

        log.info("deletePaidVendorLogical {}", paidVendorId);

        paidVendorService.deletePaidVendorLogical(paidVendorId);

        return ResponseVO.success();
    }

    @ApiOperation("查询竞价供应商")
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO<PageVO<PaidVendorVO>> findPaidVendor(PaidVendorFilterDTO paidVendorFilterDTO) {

        log.info("findPaidVendor {}", paidVendorFilterDTO);

        Validation.buildDefaultValidatorFactory().getValidator().validate(paidVendorFilterDTO);

        return ResponseVO.success(paidVendorService.queryPaidVendorWithUser(paidVendorFilterDTO));
    }



    @ApiOperation("供应商修改供应商（状态）")
    @PutMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO updatePaidVendorStatus(@RequestBody PaidVendorDTO paidVendorDTO) {

        log.info("updatePaidVendorStatus {}", paidVendorDTO.toString());

        paidVendorDTO.setUpdatedUser(getAccount());
        paidVendorService.updatePaidVendorStatus(paidVendorDTO);

        return ResponseVO.success();
    }

    @ApiOperation("新增竞价供应商并自动生成mark")
    @PostMapping(value = "saveWithMark", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO savePaidVendorWithMark(@RequestBody PaidVendorDTO paidVendorDTO) {

        log.info("savePaidVendorWithMark {}", paidVendorDTO.toString());

        paidVendorDTO.setCreatedUser(getAccount());
        paidVendorService.savePaidVendorWithMark(paidVendorDTO);

        return ResponseVO.success();
    }

    @ApiOperation("批量修改竞价供应商")
    @PutMapping(value = "/batchUpdate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO batchUpdatePaidVendor(@RequestBody List<PaidVendorDTO> paidVendorDTOS) {

        log.info("batchUpdatePaidVendor {}", paidVendorDTOS.toString());

        paidVendorDTOS.forEach(paidVendorDTO -> {
            paidVendorDTO.setUpdatedUser(getAccount());
            paidVendorService.updatePaidVendor(paidVendorDTO);
        });

        return ResponseVO.success();
    }
    @ApiOperation("批量修改竞价供应商(判断起始价格)")
    @PutMapping(value = "/batchUpdateJudgePrice", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO batchUpdateJudgePrice(@RequestBody List<PaidVendorDTO> paidVendorDTOS) {

        log.info("batchUpdateJudgePrice {}", paidVendorDTOS.toString());

        paidVendorDTOS.forEach(paidVendorDTO -> {
            paidVendorDTO.setUpdatedUser(getAccount());
            paidVendorService.updatePaidVendorJudgeStartPrice(paidVendorDTO);
        });

        return ResponseVO.success();
    }

    @ApiOperation("招标放弃授权")
    @GetMapping(value = "/waiveAuth/{paidSheetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "paidSheetId", dataType = "Long", value = "竞价单ID", required = true),
    })
    public ResponseVO<PageVO<PaidVendorVO>> waiveAuth(@PathVariable("paidSheetId")
                                                     @NotNull(message = "竞价单ID不能为空") Long paidSheetId) {

        log.info("waiveAuth {}", paidSheetId);

        return ResponseVO.success(paidVendorService.waiveAuth(paidSheetId));
    }

}
