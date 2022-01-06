package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.BidSheetDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDetailExcelDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDetailFilterDTO;
import com.xdcplus.biz.common.pojo.query.BidVendorDetailQuery;
import com.xdcplus.biz.common.pojo.vo.*;
import com.xdcplus.biz.generator.BidVendorDetailBaseService;
import com.xdcplus.biz.common.pojo.entity.BidVendorDetail;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 供应商内容明细（国内报价、国外报价）(BidVendorDetail)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-19 16:23:42
 */
public interface BidVendorDetailService extends BidVendorDetailBaseService<BidVendorDetail, BidVendorDetailVO, BidVendorDetail> {

//    void batchUpdateBidDetailDTO(BidVendorDTO bidVendorDTO);

    List<BidVendorDetailVO> queryNewBidVendorDetail(BidVendorDetailFilterDTO bidVendorDetailFilterDTO);

    List<BidVendorDetailMapVO> queryBidVendorDetailMap(BidVendorDetailFilterDTO bidVendorDetailFilterDTO);

    /**
     * 导出招标供应商报价汇总
     *
     * @param response   响应
     * @param bidSheetId 报价表的id
     */
    void exportExcelCommercial(HttpServletResponse response,Long bidSheetId);

    /**
     * 导出所有招标供应商报价
     *
     * @param response   响应
     * @param bidSheetId 报价表的id
     */
    void exportExcelbidVendorDetail(HttpServletResponse response,Long bidSheetId);

    /**
     * 导出excel所用供应商报价数据
     *
     * @param bidVendorDetailFilterDTO 投标供应商细节过滤器dto
     * @return {@link List<BidVendorDetailExcelVO>}
     */
    List<BidVendorDetailExcelVO> queryBidVendorDetailExcel(BidVendorDetailFilterDTO bidVendorDetailFilterDTO);

    List<BidVendorDetailExcelDTO> getBidVendorDetailExcelDTO(BidVendorDetailFilterDTO bidVendorDetailFilterDTO);

    void exportExcelCommercialNew(HttpServletResponse response,Long bidSheetId);

    void exportExcelCommercialNew1(HttpServletResponse response,Long bidSheetId);

    List<BidVendorDetailCollectVO> queryBidVendorDetailCollectVO(BidVendorDetailFilterDTO bidVendorDetailFilterDTO);

    List<BidVendorDetailVendorVO> queryBidVendorDetailVendorVO(BidVendorDetailFilterDTO bidVendorDetailFilterDTO);


}
