package com.xdcplus.vendor.mapper;

import com.xdcplus.vendor.common.pojo.entity.PaidSheet;
import com.xdcplus.vendor.common.pojo.query.PaidSheetQuery;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 竞价单(PaidSheet)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-07 10:33:29
 */
public interface PaidSheetMapper extends IBaseMapper<PaidSheet> {

    /**
     * 查询竞价单(PaidSheet)
     *
     * @param paidSheetQuery 竞价单(PaidSheet)查询
     * @return {@link List<PaidSheet>}
     */
    List<PaidSheet> queryPaidSheet(PaidSheetQuery paidSheetQuery);

    List<PaidSheet> queryPaidSheetByVendor(PaidSheetQuery paidSheetQuery);

    /**
     * 查询竞价单通过表单ID
     *
     * @param requestId 请求id
     * @return {@link PaidSheet}
     */
    PaidSheet findPaidSheetByRequestId(@Param("requestId") Long requestId);

    /**
     * 表单标识修改竞价状态
     *
     * @param requestId   表单标识
     * @param paidStatusMark 支付状态标志
     */
    void updatePaidStatusMarkByRequestId(@Param("requestId") Long requestId, @Param("paidStatusMark") Integer paidStatusMark,
                                         @Param("paidStatus") String paidStatus);




}
