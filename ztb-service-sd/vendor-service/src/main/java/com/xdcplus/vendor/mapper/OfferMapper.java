package com.xdcplus.vendor.mapper;

import com.xdcplus.vendor.common.pojo.bo.BidItemsBO;
import com.xdcplus.vendor.common.pojo.entity.Offer;
import com.xdcplus.vendor.common.pojo.query.OfferQuery;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 出价记录 Mapper 接口
 *
 * @author Rong.Jia
 * @since 2021-08-17
 */
public interface OfferMapper extends IBaseMapper<Offer> {

    /**
     * 根据物品标识查询出价记录
     *
     * @param offerGoods 物品标识
     * @param requestId 表单标识
     * @return {@link List <Offer>} 出价记录
     */
    List<Offer> findOfferByOfferGoods(@Param("requestId") Long requestId, @Param("offerGoods") String offerGoods);

    /**
     * 查询出价记录
     *
     * @param offerQuery 查询对象
     * @return {@link List<Offer>} 出价记录
     */
    List<Offer> findOffer(OfferQuery offerQuery);

    /**
     * 查询出价信息通过父id
     *
     * @param parentId 父id
     * @return {@link List}<{@link Offer}>
     */
    List<Offer> findOfferByParentId(@Param("parentId") Long parentId);

    /**
     * 更新可用通过id
     *
     * @param id        主键ID
     * @param available 是否有效, 0:无效，1：有效
     */
    void updateAvailableById(@Param("id") Long id, @Param("available") Byte available);

    /**
     * 更新触发阈值通过id
     *
     * @param id        主键ID
     * @param triggerThreshold 是否触发阈值，0:否，1：有
     */
    void updateTriggerThresholdById(@Param("id") Long id, @Param("triggerThreshold") Byte triggerThreshold);

    /**
     * 查询出价记录
     *
     * @param offerGoods 竞价商品标识
     * @param available  是否有效, 0:无效，1：有效
     * @param offerUser  提供用户
     * @param requestId 表单标识
     * @return {@link List}<{@link Offer}>
     */
    List<Offer> findOffers(@Param("requestId") Long requestId, @Param("offerGoods") String offerGoods,
                           @Param("available") Byte available, @Param("offerUser") String offerUser);

    /**
     * 分组最新的最大出价记录
     * @param available  是否有效, 0:无效，1：有效, 为空则都获取
     * @param offerGoods 竞价商品标识
     * @param requestId 表单标识
     * @return {@link List}<{@link Offer}>
     */
    List<Offer> latestMaxGroupOffer(@Param("requestId") Long requestId, @Param("offerGoods") String offerGoods,
                                    @Param("available") Byte available);

    /**
     * 分组最新的最小出价记录
     * @param available  是否有效, 0:无效，1：有效, 为空则都获取
     * @param offerGoods 竞价商品标识
     * @param requestId 表单标识
     * @return {@link List}<{@link Offer}>
     */
    List<Offer> latestMinGroupOffer(@Param("requestId") Long requestId, @Param("offerGoods") String offerGoods,
                                    @Param("available") Byte available);

    /**
     * 根据表单ID获取 每个供应商的每个物品的最高出价
     * @param available  是否有效, 0:无效，1：有效, 为空则都获取
     * @param requestId 表单ID
     * @param flag 是否只获取parentId不为0的数据，true: 只获取parentId为0，false:只获取parentId不为0
     * @return {@link List}<{@link Offer}>
     */
    List<Offer> groupMaxOfferByRequestId(@Param("requestId") Long requestId, @Param("available") Byte available,
                                         @Param("flag") boolean flag);

    /**
     * 根据表单ID获取 每个供应商的每个物品的最小出价
     *
     * @param available  是否有效, 0:无效，1：有效, 为空则都获取
     * @param requestId 表单ID
     * @param flag 是否只获取parentId不为0的数据，true: 只获取parentId为0，false:只获取parentId不为0
     * @return {@link List}<{@link Offer}>
     */
    List<Offer> groupMinOfferByRequestId(@Param("requestId") Long requestId, @Param("available") Byte available,
                                         @Param("flag") boolean flag);

    /**
     * 根据表单ID对父级ID进行分组
     *
     * @param requestId 请求id
     * @param available  是否有效, 0:无效，1：有效
     * @return {@link Set}<{@link Long}>
     */
    Set<Long> groupOfferParentIdsByRequestId(@Param("requestId") Long requestId, @Param("available") Byte available);

    /**
     * 查询出价物品类别
     *
     * @param requestId 表单ID
     * @param available 是否有效, 0:无效，1：有效
     * @return {@link List}<{@link BidItemsBO}>
     */
    List<BidItemsBO> findCategoryBiddingItems(@Param("requestId") Long requestId, @Param("available") Byte available);

    /**
     * 查询最新一次出价记录
     *
     * @param requestId  表单ID
     * @param offerGoods 物品标识
     * @param offerUser  出价人
     * @param available  是否有效, 0:无效，1：有效
     * @return {@link Offer}
     */
    Offer findLastOfferByOfferUser(@Param("requestId") Long requestId, @Param("offerGoods") String offerGoods,
                                   @Param("offerUser") String offerUser, @Param("available") Byte available);



































}
