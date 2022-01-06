package com.xdcplus.statement.service;

import com.xdcplus.statement.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.statement.common.pojo.vo.PaidSheetStatementVO;
import com.xdcplus.statement.generator.PaidSheetBaseService;
import com.xdcplus.statement.common.pojo.entity.PaidSheet;
import com.xdcplus.statement.common.pojo.vo.PaidSheetVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import javax.servlet.http.HttpServletResponse;


/**
 * 竞价单(PaidSheet)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:02:03
 */
public interface PaidSheetService extends PaidSheetBaseService<PaidSheet, PaidSheetVO, PaidSheet> {

    /**
     * 查询竞价单(报表)
     *
     * @param paidSheetFilterDTO 工资表过滤dto
     * @return {@link PageVO<PaidSheetVO>}
     */
    PageVO<PaidSheetStatementVO> queryPaidSheetWithStatement(PaidSheetFilterDTO paidSheetFilterDTO);

    /**
     * 导出竞价报表信息
     *
     * @param response           响应
     * @param paidSheetFilterDTO 工资表过滤dto
     */
    void exportExcelPaidSheetWithStatement(HttpServletResponse response, PaidSheetFilterDTO paidSheetFilterDTO);

}
