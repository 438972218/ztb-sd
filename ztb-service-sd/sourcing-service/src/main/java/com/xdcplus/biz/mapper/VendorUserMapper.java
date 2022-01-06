package com.xdcplus.biz.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.biz.common.pojo.entity.VendorUser;
import com.xdcplus.biz.common.pojo.query.VendorUserQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商用户(VendorUser)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:25
 */
public interface VendorUserMapper extends IBaseMapper<VendorUser> {

    /**
     * 查询供应商用户(VendorUser)
     *
     * @param vendorUserQuery 供应商用户(VendorUser)查询
     * @return {@link List<VendorUser>}
     */
    List<VendorUser> queryVendorUser(VendorUserQuery vendorUserQuery);

}
