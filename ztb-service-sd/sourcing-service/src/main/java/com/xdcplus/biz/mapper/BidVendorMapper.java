package com.xdcplus.biz.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.biz.common.pojo.entity.BidVendor;
import com.xdcplus.biz.common.pojo.query.BidVendorQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 招标投标方(BidVendor)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-10-15 13:27:59
 */
public interface BidVendorMapper extends IBaseMapper<BidVendor> {

    /**
     * 查询招标投标方(BidVendor)
     *
     * @param bidVendorQuery 招标投标方(BidVendor)查询
     * @return {@link List<BidVendor>}
     */
    List<BidVendor> queryBidVendor(BidVendorQuery bidVendorQuery);

}
