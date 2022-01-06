package com.xdcplus.statement.mapper;

import com.xdcplus.statement.common.pojo.entity.BidSpecialist;
import com.xdcplus.statement.common.pojo.query.BidSpecialistQuery;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;

import java.util.List;

/**
 * 专家(BidSpecialist)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-08-24 16:22:57
 */
public interface BidSpecialistMapper extends IBaseMapper<BidSpecialist> {

    /**
     * 查询专家(BidSpecialist)
     *
     * @param bidSpecialistQuery 专家(BidSpecialist)查询
     * @return {@link List<BidSpecialist>}
     */
    List<BidSpecialist> queryBidSpecialist(BidSpecialistQuery bidSpecialistQuery);

}
