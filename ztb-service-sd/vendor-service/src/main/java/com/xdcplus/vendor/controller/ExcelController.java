package com.xdcplus.vendor.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.xdcplus.vendor.common.pojo.vo.BidSingleRankingExcelVO;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.vendor.service.PaidMaterialService;
import com.xdcplus.vendor.service.VendorService;
import com.xdcplus.vendor.websocket.BidRankingMessage;
import com.xdcplus.ztb.common.excel.ExportExcelUtil;
import com.xdcplus.ztb.common.excel.ImportExcel;
import com.xdcplus.ztb.common.remote.domain.perm.dto.VendorExcelDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(tags = "导出excel(excel)")
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("excel")
public class ExcelController {

    @Autowired
    VendorService vendorService;

    @Autowired
    PaidMaterialService paidMaterialService;

    /**
     *
     * 导入已供应商信息
     * @param multipartFile
     */
    @PostMapping(value = "/importVendorFile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO importVendorFile(MultipartFile multipartFile){
        try {
            ImportExcel ei = new ImportExcel(multipartFile, 1, 0);
            List<VendorExcelDTO> list = ei.getDataList(VendorExcelDTO.class);

            vendorService.importVendorFile(list);
        } catch (Exception e) {
            log.error("导入失败",e);
        }
        return ResponseVO.success();
    }

    /**
     *
     * 导入已供应商信息(上线后)
     * @param multipartFile
     */
    @PostMapping(value = "/importVendorFileOnLine", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO importVendorFileOnLine(MultipartFile multipartFile){
        try {
            ImportExcel ei = new ImportExcel(multipartFile, 1, 0);
            List<VendorExcelDTO> list = ei.getDataList(VendorExcelDTO.class);

            vendorService.importVendorFileOnLine(list);
        } catch (Exception e) {
            log.error("导入失败",e);
        }
        return ResponseVO.success();
    }



    @ApiOperation("导出竞价总排名")
    @GetMapping(value = "export/paidTotalOffer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO exportExcelpaidTotalOffer(HttpServletResponse response, Long requestId) {

        paidMaterialService.exportExcelpaidTotalOffer(response,requestId);

        return ResponseVO.success();
    }

    @ApiOperation("导出竞价单排名")
    @GetMapping(value = "export/paidSingleOffer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseVO exportExcelpaidSingleOffer(HttpServletResponse response, Long requestId) {

        paidMaterialService.exportExcelpaidSingleOffer(response,requestId);

        return ResponseVO.success();
    }

}
