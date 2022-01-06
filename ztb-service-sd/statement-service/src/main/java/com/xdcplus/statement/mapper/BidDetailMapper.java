package com.xdcplus.statement.mapper;

import com.xdcplus.statement.common.pojo.vo.BidDetailCollectVO;
import com.xdcplus.statement.common.pojo.entity.BidDetail;
import com.xdcplus.statement.common.pojo.query.BidDetailQuery;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;

import java.util.List;

/**
 * 招标单内容明细（报价须知、国内报价、国外报价）(BidDetail)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-08-27 11:26:48
 */
public interface BidDetailMapper extends IBaseMapper<BidDetail> {

    /**
     * 查询招标单内容明细（报价须知、国内报价、国外报价）(BidDetail)
     *
     * @param bidDetailQuery 招标单内容明细（报价须知、国内报价、国外报价）(BidDetail)查询
     * @return {@link List<BidDetail>}
     */
    List<BidDetail> queryBidDetail(BidDetailQuery bidDetailQuery);

    List<BidDetailCollectVO> queryBidDetailCollectVO(BidDetailQuery bidDetailQuery);

}
