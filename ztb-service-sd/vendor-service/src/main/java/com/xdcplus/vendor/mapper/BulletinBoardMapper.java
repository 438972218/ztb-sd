package com.xdcplus.vendor.mapper;

import com.xdcplus.vendor.common.pojo.entity.BulletinBoard;
import com.xdcplus.vendor.common.pojo.query.BulletinBoardQuery;
import com.xdcplus.vendor.common.pojo.vo.BulletinBoardVO;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 公告牌信息Mapper 接口
 *
 * @author Rong.Jia
 * @since 2021-08-16
 */
public interface BulletinBoardMapper extends IBaseMapper<BulletinBoard> {

    /**
     * 查询公告板通过的名字
     *
     * @param name 的名字
     * @return {@link BulletinBoard} 公告板
     */
    BulletinBoard findBulletinBoardByName(@Param("name") String name);

    /**
     * 查询公告板
     *
     * @param query 查询
     * @return {@link List<BulletinBoard>}
     */
    List<BulletinBoard> findBulletinBoard(BulletinBoardQuery query);

    /**
     * 根据表单ID获取公告信息
     *
     * @param requestId 表单ID
     * @return {@link BulletinBoard}
     */
    List<BulletinBoard> findBulletinBoardByRequestId(@Param("requestId") Long requestId);

}
