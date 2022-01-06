package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.VendorKpi;
import com.xdcplus.statement.common.pojo.query.VendorKpiQuery;

import java.util.List;

/**
 * 供应商绩效考核表(VendorKpi)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-28 13:47:35
 */
public interface VendorKpiMapper extends IBaseMapper<VendorKpi> {

    /**
     * 查询供应商绩效考核表(VendorKpi)
     *
     * @param vendorKpiQuery 供应商绩效考核表(VendorKpi)查询
     * @return {@link List<VendorKpi>}
     */
    List<VendorKpi> queryVendorKpi(VendorKpiQuery vendorKpiQuery);

}
