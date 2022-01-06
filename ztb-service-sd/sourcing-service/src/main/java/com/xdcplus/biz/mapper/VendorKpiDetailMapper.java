package com.xdcplus.biz.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.biz.common.pojo.entity.VendorKpiDetail;
import com.xdcplus.biz.common.pojo.query.VendorKpiDetailQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商绩效考核表明细(VendorKpiDetail)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-28 17:37:06
 */
public interface VendorKpiDetailMapper extends IBaseMapper<VendorKpiDetail> {

    /**
     * 查询供应商绩效考核表明细(VendorKpiDetail)
     *
     * @param vendorKpiDetailQuery 供应商绩效考核表明细(VendorKpiDetail)查询
     * @return {@link List<VendorKpiDetail>}
     */
    List<VendorKpiDetail> queryVendorKpiDetail(VendorKpiDetailQuery vendorKpiDetailQuery);

}
