package com.xdcplus.statement.mapper;

import com.xdcplus.statement.common.pojo.entity.PaidSheet;
import com.xdcplus.statement.common.pojo.entity.BidSheet;
import com.xdcplus.statement.common.pojo.query.BidSheetQuery;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * bid招标单(BidSheet)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-17 17:38:15
 */
public interface BidSheetMapper extends IBaseMapper<BidSheet> {

    /**
     * 查询bid招标单(BidSheet)
     *
     * @param bidSheetQuery bid招标单(BidSheet)查询
     * @return {@link List<BidSheet>}
     */
    List<BidSheet> queryBidSheet(BidSheetQuery bidSheetQuery);

    /**
     * 查询bid招标单模板(BidSheet)
     *
     * @param bidSheetQuery bid招标单(BidSheet)查询
     * @return {@link List<BidSheet>}
     */
    List<BidSheet> queryBidSheetTemplate(BidSheetQuery bidSheetQuery);

    /**
     * 根据userId查询是项目成员的竞价单
     *
     * @param userId 用户id
     * @return {@link List<  PaidSheet  >}
     */
    List<BidSheet> queryBidSheetByProjectNumber(Long userId);

    List<BidSheet> queryBidSheetBySpecialistUserIdAndRequestIds(@Param("userId") Long userId, @Param("requestIds") List<Long> requestIds);


}
