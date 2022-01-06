package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.PaidVendor;
import com.xdcplus.statement.common.pojo.query.PaidVendorQuery;

import java.util.List;

/**
 * 竞价供应商(PaidVendor)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-16 11:57:52
 */
public interface PaidVendorMapper extends IBaseMapper<PaidVendor> {

    /**
     * 查询竞价供应商(PaidVendor)
     *
     * @param paidVendorQuery 竞价供应商(PaidVendor)查询
     * @return {@link List<PaidVendor>}
     */
    List<PaidVendor> queryPaidVendor(PaidVendorQuery paidVendorQuery);

}
