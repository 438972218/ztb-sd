package com.xdcplus.statement.controller;


import com.xdcplus.statement.common.pojo.dto.BidSheetFilterDTO;
import com.xdcplus.statement.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.statement.service.BidSheetService;
import com.xdcplus.statement.service.BidVendorDetailService;
import com.xdcplus.statement.service.PaidSheetService;
import com.xdcplus.ztb.common.mp.controller.AbstractController;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * 附件(BidAttachment)服务控制层
 *
 * @author Fish.Fei
 * @since 2021-07-06 15:16:52
 */
@Api(tags = "导出excel(excel)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("excel")
public class ExportExcelController extends AbstractController {

    @Autowired
    PaidSheetService paidSheetService;

    @Autowired
    BidSheetService bidSheetService;

    @Autowired
    BidVendorDetailService bidVendorDetailService;

//    @ApiOperation("导出所有招标供应商报价")
//    @GetMapping(value = "export/bidVendorDetail", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseVO exportExcelbidVendorDetail(HttpServletResponse response,Long bidSheetId) {
//        bidVendorDetailService.exportExcelbidVendorDetail(response,bidSheetId);
//
//        return ResponseVO.success();
//    }

    @ApiOperation("导出招标报表信息")
    @PostMapping(value = "export/bidSheetStatement", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO exportExcelBidSheetWithStatement(HttpServletResponse response, @RequestBody BidSheetFilterDTO bidSheetFilterDTO) {
        bidSheetService.exportExcelBidSheetWithStatement(response,bidSheetFilterDTO);

        return ResponseVO.success();
    }

    @ApiOperation("导出竞价报表信息")
    @PostMapping(value = "export/paidSheetStatement", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO exportExcelPaidSheetWithStatement(HttpServletResponse response, @RequestBody PaidSheetFilterDTO paidSheetFilterDTO) {
        paidSheetService.exportExcelPaidSheetWithStatement(response,paidSheetFilterDTO);

        return ResponseVO.success();
    }


//    @ApiOperation("导出招标供应商报价汇总")
//    @GetMapping(value = "/export/bidVendorDetailCommercial", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void exportExcel(HttpServletResponse response,Long bidSheetId) {
//
//        bidVendorDetailService.exportExcelCommercial(response,bidSheetId);
//    }

//    @ApiOperation("导出招标供应商报价汇总")
//    @GetMapping(value = "/export/bidVendorDetailCommercial", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void exportExcel(HttpServletResponse response,Long bidSheetId) {
//
//        bidVendorDetailService.exportExcelCommercialNew1(response,bidSheetId);
//    }

//    /**
//     *
//     * 导入已经填好数据的Excel
//     * @param multipartFile
//     */
//    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void importFile(MultipartFile multipartFile){
//        try {
//            int successNum = 0;
//            int failureNum = 0;
//            StringBuilder failureMsg = new StringBuilder();
//            ImportExcel ei = new ImportExcel(multipartFile, 1, 0);
//            List<VendorExcelDTO> list = ei.getDataList(VendorExcelDTO.class);
//
//        } catch (Exception e) {
//            log.error("导入失败",e);
//        }
//    }

}
