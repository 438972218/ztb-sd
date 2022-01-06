package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidSheetFilterDTO;
import com.xdcplus.vendor.common.pojo.entity.PaidSheet;
import com.xdcplus.vendor.common.pojo.vo.PaidSheetVO;
import com.xdcplus.vendor.generator.PaidSheetBaseService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;


/**
 * 竞价单(PaidSheet)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-31 09:40:15
 */
public interface PaidSheetService extends PaidSheetBaseService<PaidSheet, PaidSheetVO, PaidSheet> {

    /**
     * 查询竞价单(供应商)(PaidSheet)
     *
     * @param paidSheetFilterDTO 过程状态过滤DTO
     * @return {@link PageVO < PaidSheetVO >} 状态信息
     */
    PageVO<PaidSheetVO> queryPaidSheetByVendor(PaidSheetFilterDTO paidSheetFilterDTO);


    PaidSheetVO showPaidSheetByRequestIdAndVendor(Long id,Long vendorUserId);

    /**
     * 查询竞价单根据表单ID
     *
     * @param requestId 表单ID
     * @return {@link PaidSheetVO}
     */
    PaidSheetVO findPaidSheetByRequestId(Long requestId);

    /**
     * 查询竞价单竞价方向
     *
     * @param requestId 表单ID
     * @return {@link Boolean} true: 正向，false: 反向
     */
    Boolean findPaidSheetPaidDirection(Long requestId);

    /**
     * 表单标识修改竞价状态
     *
     * @param requestId     表单标识
     * @param paidStatusMark 支付状态标志
     */
    void updatePaidStatusMarkByRequestId(Long requestId, Integer paidStatusMark);

    Long getCountdownByRequestId(Long requestId);


    /**
     * 根据requestId修改竞价单
     *
     * @param paidSheetDTO 工资表dto
     * @return boolean
     */
    boolean updatePaidSheetByRequestId(PaidSheetDTO paidSheetDTO);

    /**
     * 刷新正在运行的竞价供应商标识
     */
    void refreshPaidVendorMark();


}
