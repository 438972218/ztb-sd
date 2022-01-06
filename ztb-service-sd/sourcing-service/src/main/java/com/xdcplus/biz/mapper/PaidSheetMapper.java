package com.xdcplus.biz.mapper;

import com.xdcplus.biz.common.pojo.entity.BidSheet;
import com.xdcplus.biz.common.pojo.query.BidSheetQuery;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.biz.common.pojo.entity.PaidSheet;
import com.xdcplus.biz.common.pojo.query.PaidSheetQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 竞价单(PaidSheet)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-07 10:31:20
 */
public interface PaidSheetMapper extends IBaseMapper<PaidSheet> {

    /**
     * 查询竞价单(PaidSheet)
     *
     * @param paidSheetQuery 竞价单(PaidSheet)查询
     * @return {@link List<PaidSheet>}
     */
    List<PaidSheet> queryPaidSheet(PaidSheetQuery paidSheetQuery);

    /**
     * 查询竞价单(PaidSheet)(缓存)
     *
     * @return {@link List<PaidSheet>}
     */
    List<PaidSheet> queryPaidSheetCache();

    /**
     * 根据userId查询是项目成员的竞价单
     *
     * @param userId 用户id
     * @return {@link List<PaidSheet>}
     */
    List<PaidSheet> queryPaidSheetByProjectNumber(Long userId);

    /**
     * 查询竞价单模板(PaidSheet)
     *
     * @param paidSheetQuery paid招标单(PaidSheet)查询
     * @return {@link List< PaidSheet >}
     */
    List<PaidSheet> queryPaidSheetTemplate(PaidSheetQuery paidSheetQuery);

}
