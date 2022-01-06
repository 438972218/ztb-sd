package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.dto.BulletinBoardDTO;
import com.xdcplus.vendor.common.pojo.dto.BulletinBoardFilterDTO;
import com.xdcplus.vendor.common.pojo.entity.BulletinBoard;
import com.xdcplus.vendor.common.pojo.vo.BulletinBoardVO;
import com.xdcplus.ztb.common.mp.service.BaseService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

/**
 * 公告牌信息服务类
 *
 * @author Rong.Jia
 * @since 2021-08-16
 */
public interface BulletinBoardService extends BaseService<BulletinBoard, BulletinBoardVO, BulletinBoard> {

    /**
     * 同步公告栏
     *
     * @param bulletinBoardDTO 公告董事会DTO
     * @return {@link Long} 主鍵ID
     */
    Long syncBulletinBoard(BulletinBoardDTO bulletinBoardDTO);

    /**
     * 删除公告板
     *
     * @param id 主鍵ID
     * @return {@link Boolean} 是否刪除成功
     */
    Boolean deleteBulletinBoard(Long id);

    /**
     * 查询公告板
     *
     * @param pageDTO 查詢条件
     * @return {@link PageVO<BulletinBoardVO>} 公告板信息
     */
    PageVO<BulletinBoardVO> findBulletinBoard(BulletinBoardFilterDTO pageDTO);

    /**
     * 根据表单ID获取最新的公告信息
     *
     * @param requestId 表单ID
     * @return {@link BulletinBoardVO}
     */
    BulletinBoardVO findLatestAnnouncementByRequestId(Long requestId);

















}
