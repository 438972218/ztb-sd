package com.xdcplus.statement.service;

import com.xdcplus.statement.common.pojo.dto.BidSheetFilterDTO;
import com.xdcplus.statement.common.pojo.vo.BidSheetStatementVO;
import com.xdcplus.statement.generator.BidSheetBaseService;
import com.xdcplus.statement.common.pojo.entity.BidSheet;
import com.xdcplus.statement.common.pojo.vo.BidSheetVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import javax.servlet.http.HttpServletResponse;


/**
 * bid招标单(BidSheet)表服务接口层
 *
 * @author makejava
 * @since 2021-08-12 14:45:42
 */
public interface BidSheetService extends BidSheetBaseService<BidSheet, BidSheetVO, BidSheet> {

    /**
     * 查询招标单(报表)
     *
     * @param bidSheetFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<  BidSheetStatementVO  >} 状态信息
     */
    PageVO<BidSheetStatementVO> queryBidSheetWithStatement(BidSheetFilterDTO bidSheetFilterDTO);

    /**
     * 导出招标报表
     *
     * @param response          响应
     * @param bidSheetFilterDTO 报价表过滤dto
     */
    void exportExcelBidSheetWithStatement(HttpServletResponse response, BidSheetFilterDTO bidSheetFilterDTO);

}
