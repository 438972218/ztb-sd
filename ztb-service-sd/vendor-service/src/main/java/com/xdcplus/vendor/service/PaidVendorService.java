package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.dto.PaidVendorFilterDTO;
import com.xdcplus.vendor.generator.PaidVendorBaseService;
import com.xdcplus.vendor.common.pojo.entity.PaidVendor;
import com.xdcplus.vendor.common.pojo.vo.PaidVendorVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;
import java.util.Map;


/**
 * 竞价供应商(PaidVendor)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-31 09:51:42
 */
public interface PaidVendorService extends PaidVendorBaseService<PaidVendor, PaidVendorVO, PaidVendor> {

    /**
     * 根据sheetId和userName查询竞价供应商
     *
     * @param userName    用户名
     * @param paidSheetId 工资表的id
     * @return {@link PaidVendorVO}
     */
    PaidVendorVO findPaidVendorVOByUserNameAndSheetId(String userName,Long paidSheetId);

    /**
     * 根据requestId和userName查询竞价供应商
     *
     * @param userName    用户名
     * @param requestId
     * @return {@link PaidVendorVO}
     */
    PaidVendorVO findPaidVendorVOByUserNameAndRequestId(String userName,Long requestId);

    /**
     * 根据requestId查询竞价供应商
     *
     * @param requestId 请求id
     * @return {@link List<PaidVendorVO>}
     */
    List<PaidVendorVO> findPaidVendorVOByRequestId(Long requestId);

    /**
     * 根据requestId查询竞价供应商
     *
     * @param requestId 请求id
     * @return {@link List<PaidVendorVO>}
     */
    Map<Long,List<PaidVendorVO>> findPaidVendorVOMapByRequestId(Long requestId);

    /**
     * 根据requestId和用户id查询竞价供应商
     *
     * @param requestId 请求id
     * @return {@link List<PaidVendorVO>}
     */
    PaidVendorVO findPaidVendorVOByRequestIdAndUserId(Long requestId,Long userId);

    /**
     * 查询竞价供应商(PaidVendor)WithUser
     *
     * @param paidVendorFilterDTO 过程状态过滤DTO
     * @return {@link PageVO <PaidVendorVO>} 状态信息
     */
    PageVO<PaidVendorVO> queryPaidVendorWithUser(PaidVendorFilterDTO paidVendorFilterDTO);

    /**
     * 刷新竞价供应商标识
     *
     * @param paidVendorVOS 支付供应商vos
     */
    void refreshPaidVendorMark(List<PaidVendorVO> paidVendorVOS);
}
