package com.xdcplus.biz.mapper;

import com.xdcplus.biz.common.pojo.vo.BidVendorDetailCollectVO;
import com.xdcplus.biz.common.pojo.vo.BidVendorDetailMapVO;
import com.xdcplus.biz.common.pojo.vo.BidVendorDetailVendorVO;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.biz.common.pojo.entity.BidVendorDetail;
import com.xdcplus.biz.common.pojo.query.BidVendorDetailQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 供应商内容明细（国内报价、国外报价）(BidVendorDetail)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-08-27 11:27:30
 */
public interface BidVendorDetailMapper extends IBaseMapper<BidVendorDetail> {

    /**
     * 查询供应商内容明细（国内报价、国外报价）(BidVendorDetail)
     *
     * @param bidVendorDetailQuery 供应商内容明细（国内报价、国外报价）(BidVendorDetail)查询
     * @return {@link List<BidVendorDetail>}
     */
    List<BidVendorDetail> queryBidVendorDetail(BidVendorDetailQuery bidVendorDetailQuery);

    List<BidVendorDetail> queryNewBidVendorDetail(BidVendorDetailQuery bidVendorDetailQuery);

    List<BidVendorDetailMapVO> queryBidVendorDetailMap(BidVendorDetailQuery bidVendorDetailQuery);

    List<BidVendorDetailCollectVO> queryBidVendorDetailCollectVO(BidVendorDetailQuery bidVendorDetailQuery);

    List<BidVendorDetailVendorVO> queryBidVendorDetailVendorVO(BidVendorDetailQuery bidVendorDetailQuery);

}
