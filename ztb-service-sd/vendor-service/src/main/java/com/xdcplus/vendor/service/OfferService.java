package com.xdcplus.vendor.service;


import com.xdcplus.vendor.common.pojo.dto.OfferDTO;
import com.xdcplus.vendor.common.pojo.dto.OfferFilterDTO;
import com.xdcplus.vendor.common.pojo.entity.Offer;
import com.xdcplus.vendor.common.pojo.vo.OfferVO;
import com.xdcplus.ztb.common.mp.service.BaseService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 出价记录 服务类
 * @author Rong.Jia
 * @since 2021-08-17
 */
public interface OfferService extends BaseService<Offer, OfferVO, Offer> {

    /**
     * 保存出价记录
     *
     * @param offerDTO 出价DTO
     * @return {@link OfferVO} 出价信息
     */
    OfferVO saveOffer(OfferDTO offerDTO);

    /**
     * 查询一个
     *
     * @param offerId 出价记录ID
     * @return {@link OfferVO}
     */
    OfferVO findOne(Long offerId);

    /**
     * 获取排名
     *
     * @param offerId 出价记录ID
     * @param positive 正向排名， true: 正向。false: 反向
     * @return {@link Integer} 排名
     */
    Integer getRanking(Long offerId, Boolean positive);

    /**
     * 根据物品标识查询出价记录
     *
     * @param offerGoods 物品标识
     * @param requestId 表单ID
     * @param positive 正向排名， true: 正向。false: 反向
     * @return {@link List<OfferVO>} 出价记录
     */
    List<OfferVO> findOfferByOfferGoods(Long requestId, String offerGoods, Boolean positive);

    /**
     * 查询出价记录
     *
     * @param filterDTO 过滤器DTO
     * @return {@link PageVO<OfferVO>} 出价记录
     */
    PageVO<OfferVO> findOffer(OfferFilterDTO filterDTO);

    /**
     * 设置排名
     *
     * @param offerVO  出价记录VO
     * @param positive 正向排名， true: 正向。false: 反向
     */
    void getRanking(OfferVO offerVO, Boolean positive);

    /**
     * 更新可用通过id
     *
     * @param id        主键ID
     * @param available 是否有效, 0:无效，1：有效
     */
    void updateAvailableById(Long id, Byte available);

    /**
     * 更新触发阈值通过id
     *
     * @param id        主键ID
     * @param triggerThreshold 是否触发阈值，0:否，1：有
     */
    void updateTriggerThresholdById(Long id, Byte triggerThreshold);

    /**
     * 查询出价记录
     *
     * @param offerGoods 提供商品
     * @param available  可用 0：否，1：是
     * @param offerUser  出价人呢
     * @param requestId 表单标识
     * @return {@link List}<{@link OfferVO}>
     */
    List<OfferVO> findOffer(Long requestId, String offerGoods, Byte available, String offerUser);

    /**
     * 根据物品，标识查询 最新排名的出价记录
     *
     * @param offerGoods 物品标识
     * @param requestId 表单标识
     * @param positive 正向排名， true: 正向。false: 反向
     * @return {@link List<OfferVO>} 出价记录
     */
    List<OfferVO> getLatestRanking(Long requestId, String offerGoods, Boolean positive);

    /**
     * 根据表单ID获取 每个供应商的每个物品的最高/最小出价
     *
     * @param requestId 表单ID
     * @param positive  方向， true: 正向。false: 反向
     * @return {@link List}<{@link OfferVO}>
     */
    List<OfferVO> groupOfferByRequestId(Long requestId, Boolean positive);

    /**
     * 根据表单ID获取 每个供应商的每个物品的最高/最小出价
     *
     * @param requestId 表单ID
     * @param available  是否有效, 0:无效，1：有效, 为空则都获取
     * @param positive  方向， true: 正向。false: 反向
     * @return {@link List}<{@link OfferVO}>
     */
    List<OfferVO> groupOfferByRequestId(Long requestId, Boolean positive, Byte available);

    /**
     * 删除出价记录根据表单ID
     *
     * @param requestId 表单ID
     * @return {@link Boolean}
     */
    Boolean deleteOfferByRequestId(Long requestId);

    /**
     * 根据表单ID验证是否存在父-子级物品出价记录 （parentId 不为0的记录）
     *
     * @param requestId 表单ID
     * @param available  是否有效, 0:无效，1：有效, 为空则都获取
     * @return {@link Boolean} true: 存在，false: 不存在
     */
    Boolean existenceSubidentityByRequestId(Long requestId, Byte available);

    /**
     * 计算排名
     *
     * @param offerVOList 出价记录
     * @param direction   方向 true: 正向。false: 反向
     * @return {@link List}<{@link OfferVO}>
     */
    List<OfferVO> computingRankings(List<OfferVO> offerVOList, Boolean direction);

    /**
     * 查询出价物品类别, 只获取有效数据
     *
     * @param requestId 表单ID
     * @return {@link Map}<{@link String}, {@link Set}<{@link String}>> key: 出价人，value: 出价物品
     */
    Map<String, Set<String>> findCategoryBiddingItems(Long requestId);


















}
