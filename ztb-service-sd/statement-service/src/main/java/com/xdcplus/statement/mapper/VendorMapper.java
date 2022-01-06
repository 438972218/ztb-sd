package com.xdcplus.statement.mapper;

import com.xdcplus.statement.common.pojo.vo.VendorVO;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.Vendor;
import com.xdcplus.statement.common.pojo.query.VendorQuery;

import java.util.List;

/**
 * 供应商(Vendor)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-08 14:30:26
 */
public interface VendorMapper extends IBaseMapper<Vendor> {

    /**
     * 查询供应商(Vendor)
     *
     * @param vendorQuery 供应商(Vendor)查询
     * @return {@link List<Vendor>}
     */
    List<Vendor> queryVendor(VendorQuery vendorQuery);

    /**
     * 查询供应商(Vendor)
     *
     * @param id
     * @return {@link List<Vendor>}
     */
    VendorVO showVendorByUserId(Long id);

}
