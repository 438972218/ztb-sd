package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.BidSpecialistScore;
import com.xdcplus.statement.common.pojo.query.BidSpecialistScoreQuery;

import java.util.List;

/**
 * 招标专家评分(BidSpecialistScore)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-10-27 13:48:41
 */
public interface BidSpecialistScoreMapper extends IBaseMapper<BidSpecialistScore> {

    /**
     * 查询招标专家评分(BidSpecialistScore)
     *
     * @param bidSpecialistScoreQuery 招标专家评分(BidSpecialistScore)查询
     * @return {@link List<BidSpecialistScore>}
     */
    List<BidSpecialistScore> queryBidSpecialistScore(BidSpecialistScoreQuery bidSpecialistScoreQuery);

}
