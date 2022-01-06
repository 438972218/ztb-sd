package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.xdcplus.vendor.common.constants.ZtbConstant;
import com.xdcplus.vendor.common.enums.BidRuleEnum;
import com.xdcplus.vendor.common.enums.BidStructure;
import com.xdcplus.vendor.common.enums.DirectionOfEnum;
import com.xdcplus.vendor.common.pojo.cache.BiddingSummaryInfoCache;
import com.xdcplus.vendor.common.pojo.dto.BulletinBoardDTO;
import com.xdcplus.vendor.common.pojo.dto.OfferDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.vendor.common.pojo.vo.OfferVO;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.vendor.common.pojo.vo.PaidSheetVO;
import com.xdcplus.vendor.common.pojo.vo.PaidVendorVO;
import com.xdcplus.vendor.quartz.BiddingTimeTask;
import com.xdcplus.vendor.service.*;
import com.xdcplus.vendor.websocket.*;
import com.xdcplus.ztb.common.cache.RedisUtils;
import com.xdcplus.ztb.common.quartz.QuartzTask;
import com.xdcplus.ztb.common.quartz.QuartzUtils;
import com.xdcplus.ztb.common.remote.domain.sourcing.PaidMaterialInfoVO;
import com.xdcplus.ztb.common.remote.domain.sourcing.PaidSheetInfoVO;
import com.xdcplus.ztb.common.tool.constants.CacheConstant;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 监控投标服务实现
 *
 * @author Rong.Jia
 * @date 2021/08/23
 */
@Slf4j
@Service
public class MonitorBiddingServiceImpl implements MonitorBiddingService {

    private static final String FROM_USER_ID = "server";
    private static final String BIDDING_TIME_TASK = "biddingTimeTask";

    @Autowired
    private VendorUserService vendorUserService;

    @Autowired
    private OfferService offerService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private PaidSheetService paidSheetService;

    @Autowired
    private PaidVendorService paidVendorService;

    @Autowired
    private BulletinBoardService bulletinBoardService;

    @Autowired
    private Scheduler scheduler;

    @Override
    @Async
    public void messageHandler(String message, String userId) {

        if (StrUtil.isNotBlank(message)) {
            if (JSONValidator.from(message).validate()) {
                ReceiveMessage receiveMessage = JSONObject.parseObject(message, ReceiveMessage.class);
                Integer type = receiveMessage.getType();
                switch (type) {
                    case 1:
                        biddingHandler(receiveMessage.getMessage(), userId);
                        break;
                    case 2:
                        announcementHandler(receiveMessage.getMessage(), userId);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    @Async
    public void sendClientState() {

        try {
            List<String> clientUsers = WebSocketServer.getClientUsers();
            List<BiddingSummaryInfoCache> biddingSummaryInfoCaches = getBiddingSummaryInfoCache();
            if (CollectionUtil.isEmpty(clientUsers) || CollectionUtil.isEmpty(biddingSummaryInfoCaches)) {
                log.debug("No client online or bidding summary information");
                return;
            }

            for (BiddingSummaryInfoCache biddingSummaryInfoCache : biddingSummaryInfoCaches) {
                String tendererUserId = Convert.toStr(biddingSummaryInfoCache.getTendererUserId());
                if (clientUsers.contains(tendererUserId)) {
                    List<StateMessage> stateMessageList = biddingSummaryInfoCache.getVendorSummaries()
                            .stream().map(a -> {
                                Integer state = clientUsers.contains(Convert.toStr(a.getVendorUserId())) ? NumberConstant.ONE : NumberConstant.ZERO;
                                return getStateMessage(state, NumberConstant.A_NEGATIVE, a.getVendorId(), a.getVendorMark(), biddingSummaryInfoCache.getRequestId());
                            }).collect(Collectors.toList());
                    sendMessage(FROM_USER_ID, tendererUserId, NumberConstant.TWO, stateMessageList);
                }
            }
        } catch (Exception e) {
            log.error("sendClientState {}", e.getMessage());
        }
    }

    @Override
    @Async
    public void sendBiddingTime(@Nullable Long requestId, @Nullable String userId, Long finalBiddingTime) {

        // key: 物品标识, 竞价简要信息
        List<BiddingSummaryInfoCache> biddingSummaryInfoCaches = getBiddingSummaryInfoCache();
        if (CollectionUtil.isEmpty(biddingSummaryInfoCaches)) {
            log.debug("There are no bidding goods for now");
            return;
        }

        if (Validator.isNotNull(requestId)) {
            biddingSummaryInfoCaches = biddingSummaryInfoCaches.stream().filter(a -> Validator.equal(requestId, a.getRequestId())).collect(Collectors.toList());
        }

        if (CollectionUtil.isEmpty(biddingSummaryInfoCaches)) {
            log.debug("There are no bidding goods for now");
            return;
        }

        if (StrUtil.isBlank(userId)) {
            for (BiddingSummaryInfoCache biddingSummaryInfoCache : biddingSummaryInfoCaches) {
                Long currentTime = DateUtil.current();
                if (Validator.equal(NumberConstant.TWO, biddingSummaryInfoCache.getPaidStatus())) {
                    refreshBiddingStatus(biddingSummaryInfoCache, biddingSummaryInfoCache.getPaidStatus());
                    continue;
                }
                long countdown = biddingSummaryInfoCache.getEndTime() - currentTime;
                BiddingTimeMessage biddingTimeMessage = BiddingTimeMessage.builder()
                        .computationTime(currentTime)
                        .endTime(biddingSummaryInfoCache.getEndTime())
                        .startTime(biddingSummaryInfoCache.getStartTime())
                        .offerGoods(biddingSummaryInfoCache.getOfferGoods())
                        .requestId(biddingSummaryInfoCache.getRequestId())
                        .delayedTime(NumberConstant.ZERO.longValue())
                        .countdown(Math.max(countdown, NumberConstant.ZERO.longValue()))
                        .timeOfDuration(currentTime - biddingSummaryInfoCache.getStartTime())
                        .build();

                // 更新竞价状态
                if (biddingSummaryInfoCache.getEndTime() <= currentTime) {
                    paidSheetService.updatePaidStatusMarkByRequestId(biddingSummaryInfoCache.getRequestId(), NumberConstant.FOUR);
                    refreshBiddingStatus(biddingSummaryInfoCache, NumberConstant.FOUR);
                    refreshBiddingSummaryInfoCache();
                }

                Set<String> toUserIds = biddingSummaryInfoCache.getVendorSummaries().stream().map(a -> Convert.toStr(a.getVendorUserId())).collect(Collectors.toSet());
                toUserIds.add(Convert.toStr(biddingSummaryInfoCache.getTendererUserId()));
                sendMessage(FROM_USER_ID, toUserIds, NumberConstant.THREE, biddingTimeMessage);
            }
        } else {

            biddingSummaryInfoCaches = biddingSummaryInfoCaches.stream().filter(a -> Validator.equal(userId, Convert.toStr(a.getTendererUserId())) || a.getVendorSummaries().stream().anyMatch(b -> Validator.equal(Convert.toStr(b.getVendorUserId()), userId))).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(biddingSummaryInfoCaches)) {
                for (BiddingSummaryInfoCache biddingSummaryInfoCache : biddingSummaryInfoCaches) {

                    Long endTime = biddingSummaryInfoCache.getEndTime();
                    Long delayInterval = TimeUnit.MINUTES.toMillis(biddingSummaryInfoCache.getDelayInterval());
                    Long triggeringTime = endTime - delayInterval;
                    if (finalBiddingTime > triggeringTime) {
                        endTime = DateUtil.offsetMinute(new Date(finalBiddingTime), Convert.toInt(biddingSummaryInfoCache.getDelayInterval())).getTime();
                    }

                    updateBiddingEndTime(biddingSummaryInfoCache.getRequestId(), endTime);
//                        Long delayedTime = endTime - biddingSummaryInfoCache.getEndTime();

//                        BiddingTimeMessage biddingTimeMessage = BiddingTimeMessage.builder()
//                                .requestId(biddingSummaryInfoCache.getRequestId())
//                                .computationTime(finalBiddingTime)
//                                .endTime(endTime)
//                                .startTime(biddingSummaryInfoCache.getStartTime())
//                                .offerGoods(biddingSummaryInfoCache.getOfferGoods())
//                                .delayedTime(delayedTime)
//                                .countdown(endTime - finalBiddingTime)
//                                .timeOfDuration(finalBiddingTime - biddingSummaryInfoCache.getStartTime())
//                                .build();
//
//                        Set<String> toUserIds = biddingSummaryInfoCache.getVendorSummaries().stream().map(b -> Convert.toStr(b.getVendorUserId())).collect(Collectors.toSet());
//                        toUserIds.add(Convert.toStr(biddingSummaryInfoCache.getTendererUserId()));
//                        sendMessage(FROM_USER_ID, toUserIds, NumberConstant.THREE, biddingTimeMessage);

                }
            }
        }
    }

    @Override
    public void refreshBiddingSummaryInfoCache() {

        // key: 物品ID, 竞价信息
        Map<String, BiddingSummaryInfoCache> biddingSummaryInfoCacheMap = MapUtil.newHashMap();

        List<PaidSheetInfoVO> paidSheetInfos = getPaidSheetInfosCache();
        if (CollectionUtil.isNotEmpty(paidSheetInfos)) {

            for (PaidSheetInfoVO paidSheetInfo : paidSheetInfos) {
                List<PaidMaterialInfoVO> paidMaterialInfoVOList = paidSheetInfo.getPaidMaterialVOS();
                if (CollectionUtil.isNotEmpty(paidMaterialInfoVOList)) {
                    for (PaidMaterialInfoVO paidMaterialInfoVO : paidMaterialInfoVOList) {
                        BiddingSummaryInfoCache biddingSummaryInfoCache = new BiddingSummaryInfoCache();
                        BeanUtil.copyProperties(paidSheetInfo, biddingSummaryInfoCache);
                        biddingSummaryInfoCache.setRequestId(paidSheetInfo.getRequestId());
                        biddingSummaryInfoCache.setEndTime(paidSheetInfo.getOfferEndTime());
                        biddingSummaryInfoCache.setStartTime(paidSheetInfo.getOfferStartTime());
                        biddingSummaryInfoCache.setOfferGoods(Convert.toStr(paidMaterialInfoVO.getId()));
                        biddingSummaryInfoCache.setPaidStatus(paidSheetInfo.getPaidStatusMark());
                        biddingSummaryInfoCache.setPaidDirection(getPaidDirection(paidSheetInfo.getPaidDirection()));
                        biddingSummaryInfoCache.setMoreItems(getBidStructure(paidSheetInfo.getBidStructure()));
                        biddingSummaryInfoCache.setTotalQuantityArticles(paidMaterialInfoVOList.size());

                        if (ObjectUtil.isNull(paidSheetInfo.getSysUserInfoVO())) {
                            continue;
                        }

                        // 设置招标员用户
                        biddingSummaryInfoCache.setTendererUserId(paidSheetInfo.getSysUserInfoVO().getId());

                        try {
                            // 设置供应商摘要信息
                            List<PaidVendorVO> paidVendorVOList = paidVendorService.findPaidVendorVOByRequestId(paidSheetInfo.getRequestId());
                            if (CollectionUtil.isEmpty(paidVendorVOList)) {
                                continue;
                            }

                            Set<BiddingSummaryInfoCache.VendorSummary> vendorSummaries = paidVendorVOList.stream()
                                    .map(a -> BeanUtil.copyProperties(a, BiddingSummaryInfoCache.VendorSummary.class))
                                    .collect(Collectors.toSet());
                            biddingSummaryInfoCache.setVendorSummaries(vendorSummaries);
                        }catch (Exception e) {
                            log.error("refreshBiddingSummaryInfoCache {}", e.getMessage());
                            continue;
                        }

                        biddingSummaryInfoCacheMap.put(Convert.toStr(paidMaterialInfoVO.getId()), biddingSummaryInfoCache);
                    }
                }
            }
        }

        if (MapUtil.isNotEmpty(biddingSummaryInfoCacheMap)) {
            setBiddingSummaryInfoCache(biddingSummaryInfoCacheMap.values());
        }
    }

    @Override
    public void addBiddingTimeTimedTask() {

        QuartzTask quartzTask = QuartzTask.builder()
                .jobName(BIDDING_TIME_TASK)
                .cronExpression("0/1 * * * * ?")
                .jobClass(BiddingTimeTask.class)
                .build();

        QuartzUtils.createScheduleJob(scheduler, quartzTask);
    }

    @Override
    public void refreshVendorMark() {

        paidSheetService.refreshPaidVendorMark();
        List<BiddingSummaryInfoCache> biddingSummaryInfoCaches = getBiddingSummaryInfoCache();
        List<String> clientUsers = WebSocketServer.getClientUsers();
        if (CollectionUtil.isNotEmpty(biddingSummaryInfoCaches) && CollectionUtil.isNotEmpty(clientUsers)) {
            Set<String> userIds = biddingSummaryInfoCaches.stream()
                    .map(a -> Convert.toStr(a.getTendererUserId()))
                    .filter(clientUsers::contains).collect(Collectors.toSet());
            for (String userId : userIds) {
                sendMessage(FROM_USER_ID, userId, NumberConstant.A_NEGATIVE, Boolean.TRUE);
            }
        }
    }

    /**
     * 公告处理
     *
     * @param message 消息
     * @param userId  用户id
     */
    private void announcementHandler(Object message, String userId) {

        if (ObjectUtil.isNull(vendorUserService.findVendorUserByUserId(Convert.toLong(userId)))) {
            AnnouncementMessage announcementMessage = JSONObject.parseObject(JSON.toJSONString(message), AnnouncementMessage.class);
            BulletinBoardDTO bulletinBoardDTO = BeanUtil.copyProperties(announcementMessage, BulletinBoardDTO.class);
            bulletinBoardService.syncBulletinBoard(bulletinBoardDTO);

            List<PaidVendorVO> paidVendorVOList = paidVendorService.findPaidVendorVOByRequestId(announcementMessage.getRequestId());
            if (CollectionUtil.isNotEmpty(paidVendorVOList)) {
                Set<Long> vendorUserIds = paidVendorVOList.stream().map(PaidVendorVO::getVendorUserId).collect(Collectors.toSet());
                for (Long vendorUserId : vendorUserIds) {
                    sendMessage(userId, Convert.toStr(vendorUserId), NumberConstant.FOUR, announcementMessage);
                }
            }
        }
    }

    /**
     * 竞价处理
     *
     * @param message 消息
     * @param userId  用户id
     */
    private void biddingHandler(Object message, String userId) {

        BiddingMessage biddingMessage = JSONObject.parseObject(JSON.toJSONString(message), BiddingMessage.class);

        // 计算有效，阈值
        calculationOffer(biddingMessage);

        // 有效-计算竞价时间
        if(ObjectUtil.equal(NumberConstant.ONE.byteValue(), biddingMessage.getAvailable())){
            this.sendBiddingTime(biddingMessage.getRequestId(), userId, DateUtil.current());
        }

        OfferDTO offerDTO = JSONObject.parseObject(JSON.toJSONString(biddingMessage), OfferDTO.class);

        // 保存出价记录
        OfferVO offerVO = offerService.saveOffer(offerDTO);

        PaidSheetVO paidSheetInfoVO = paidSheetService.findPaidSheetByRequestId(offerVO.getRequestId());
        if (ObjectUtil.isNotNull(paidSheetInfoVO)) {

            // 正向排名， true: 正向。false: 反向
            Boolean direction = getPaidDirection(paidSheetInfoVO.getPaidDirection());

            offerService.getRanking(offerVO, direction);

            // 推送自己排名
            sendMessage(userId, userId, NumberConstant.ONE, offerVO);

            List<BiddingSummaryInfoCache> biddingSummaryInfoCaches = getBiddingSummaryInfoCache();
            if (CollectionUtil.isNotEmpty(biddingSummaryInfoCaches)) {

                BiddingSummaryInfoCache biddingSummaryInfoCache = biddingSummaryInfoCaches.stream()
                        .filter(a -> Validator.equal(offerVO.getRequestId(), a.getRequestId())).findAny().orElse(null);

                if (ObjectUtil.isNotNull(biddingSummaryInfoCache)) {
                    sendMessage(FROM_USER_ID, Convert.toStr(biddingSummaryInfoCache.getTendererUserId()), NumberConstant.ONE, offerVO);
                }
            }

            refreshVendorRanking(offerVO.getRequestId(), direction);
        }
    }

    /**
     * 刷新竞价状态
     *
     * @param biddingSummaryInfoCache  竞价信息缓存
     * @param paidStatus 竞价状态，1：进行中，2：暂停，3：已终止，4：截止
     */
    private void refreshBiddingStatus(BiddingSummaryInfoCache biddingSummaryInfoCache, Integer paidStatus) {

        List<String> clientUsers = WebSocketServer.getClientUsers();
        if (CollectionUtil.isNotEmpty(clientUsers)) {
            Integer tendererStatus = clientUsers.contains(Convert.toStr(biddingSummaryInfoCache.getTendererUserId())) ? NumberConstant.ONE : NumberConstant.ZERO;
            sendMessage(FROM_USER_ID, Convert.toStr(biddingSummaryInfoCache.getTendererUserId()),
                    NumberConstant.TWO, getStateMessage(tendererStatus, paidStatus, biddingSummaryInfoCache.getRequestId()));
            if (CollectionUtil.isNotEmpty(biddingSummaryInfoCache.getVendorSummaries())) {
                for (BiddingSummaryInfoCache.VendorSummary vendorSummary : biddingSummaryInfoCache.getVendorSummaries()) {
                    Integer status = clientUsers.contains(Convert.toStr(vendorSummary.getVendorUserId())) ? NumberConstant.ONE : NumberConstant.ZERO;
                    StateMessage stateMessage = getStateMessage(status, paidStatus, vendorSummary.getVendorId(),
                            vendorSummary.getVendorMark(), biddingSummaryInfoCache.getRequestId());
                    sendMessage(Convert.toStr(biddingSummaryInfoCache.getTendererUserId()),
                            Convert.toStr(vendorSummary.getVendorUserId()), NumberConstant.TWO, stateMessage);
                }
            }
        }
    }

    /**
     * 修改竞价结束时间
     *
     * @param requestId 表单ID
     * @param endTime   结束时间
     */
    private void updateBiddingEndTime(Long requestId, Long endTime) {
        PaidSheetDTO paidSheetDTO = new PaidSheetDTO();
        paidSheetDTO.setRequestId(requestId);
        paidSheetDTO.setOfferEndTime(endTime);
        paidSheetService.updatePaidSheetByRequestId(paidSheetDTO);
        refreshBiddingSummaryInfoCache();
    }

    /**
     * 设置投标简要信息缓存
     *
     * @param biddingSummaryInfoCaches 投标摘要信息缓存
     */
    private void setBiddingSummaryInfoCache(Collection<BiddingSummaryInfoCache> biddingSummaryInfoCaches) {
        if (CollectionUtil.isNotEmpty(biddingSummaryInfoCaches)) {
            redisUtils.set(CacheConstant.BIDDING_SUMMARY_CACHE, JSON.toJSONString(biddingSummaryInfoCaches));
        }
    }

    /**
     * 获取状态消息
     *
     * @param state      状态
     * @param paidStatus 支付状态
     * @param vendorId   供应商id
     * @param requestId 表单ID
     * @param vendorMark 供应商标识
     * @return {@link StateMessage}
     */
    private StateMessage getStateMessage(Integer state, Integer paidStatus, Long vendorId, String vendorMark, Long requestId) {

        StateMessage stateMessage = new StateMessage();
        stateMessage.setState(state);
        stateMessage.setPaidStatus(paidStatus);
        stateMessage.setVendorId(vendorId);
        stateMessage.setVendorMark(vendorMark);
        stateMessage.setRequestId(requestId);

        return stateMessage;
    }

    /**
     * 获取状态消息
     *
     * @param state      状态
     * @param paidStatus 支付状态
     * @param requestId 表单ID
     * @return {@link StateMessage}
     */
    private StateMessage getStateMessage(Integer state, Integer paidStatus, Long requestId) {

        StateMessage stateMessage = new StateMessage();
        stateMessage.setState(state);
        stateMessage.setPaidStatus(paidStatus);
        stateMessage.setRequestId(requestId);

        return stateMessage;
    }

    /**
     * 刷新供应商排名
     *
     *  <p>
     *      排名方式：
     *      1. 单竞价单物品排名：
     *          1. 总/单排名：
     *              1. 单竞价内
     *              2. 每个物品
     *              3. 每个供应商的出价最高/低价
     *              5. 排名
     *      2. 单竞价多物品排名：
     *          1. 总排名：
     *              1. 单竞价内
     *              2. 每个供应商
     *              3. 每个物品的出价最高/低价
     *              5. 和
     *              6. 排名
     *          2. 单排名
     *              同单竞价单物品排名
     *  </p>
     *
     * @param requestId     表单标识
     * @param direction     方向 true: 正向。false: 反向
     */
    private void refreshVendorRanking(Long requestId, Boolean direction) {

        List<BiddingSummaryInfoCache> biddingSummaryInfoCaches = getBiddingSummaryInfoCache();
        List<OfferVO> offerVOList = offerService.groupOfferByRequestId(requestId, direction, NumberConstant.ONE.byteValue());

        // key: 出价人，value: 出价物品
        Map<String, Set<String>> categoryBiddingItems = offerService.findCategoryBiddingItems(requestId);
        if (CollectionUtil.isEmpty(offerVOList) || CollectionUtil.isEmpty(biddingSummaryInfoCaches) || CollectionUtil.isEmpty(categoryBiddingItems)) {
            log.error("Bid record or bid cache or bid item incomplete");
            return;
        }

        BiddingSummaryInfoCache biddingSummaryInfoCache = biddingSummaryInfoCaches.stream()
                .filter(a -> Validator.equal(requestId, a.getRequestId())).findAny().orElse(null);
        if (ObjectUtil.isNull(biddingSummaryInfoCache)) {
            log.error("There is no bidding information of 'requestId' {} form in the bidding cache", requestId);
            return;
        }

        List<BidRankingMessage.BidRanking> totalRanking = null;

        // key: 物品标识，value: 排名
        Map<String, List<BidRankingMessage.BidRanking>> singleRanking = null;

        // 是否是多物品竞价，true: 是，false: 否
        Boolean moreItems = biddingSummaryInfoCache.getMoreItems();
        if (moreItems) {

            // key: 出价人，value: 出价信息
            Map<String, OfferVO> offerUserMap = offerVOList.stream().collect(Collectors.groupingBy(OfferVO::getOfferUser))
                    .entrySet().stream()
                    .filter(x -> categoryBiddingItems.containsKey(x.getKey())
                            && categoryBiddingItems.get(x.getKey()).size() == biddingSummaryInfoCache.getTotalQuantityArticles())
                    .collect(Collectors.toMap(Map.Entry::getKey, a -> {
                        Double totalValue = a.getValue().stream()
                                .map(b -> Convert.toDouble(b.getMoney()))
                                .mapToDouble(Double::doubleValue).sum();
                        OfferVO offerVO = BeanUtil.copyProperties(a.getValue().get(NumberConstant.ZERO), OfferVO.class);
                        a.getValue().stream().max(Comparator.comparing(OfferVO::getOfferTime)).ifPresent(c -> BeanUtil.copyProperties(c, offerVO));
                        offerVO.setMoney(Convert.toStr(totalValue));
                        return offerVO;
                    }));

            totalRanking = getTotalRanking(CollectionUtil.newArrayList(offerUserMap.values()), biddingSummaryInfoCache.getVendorSummaries(), direction);
            singleRanking = getSingleRanking(offerVOList, biddingSummaryInfoCache.getVendorSummaries(), direction);
        }else {

            singleRanking = getSingleRanking(offerVOList, biddingSummaryInfoCache.getVendorSummaries(), direction);
            totalRanking = getTotalRanking(offerVOList,biddingSummaryInfoCache.getVendorSummaries(), direction);
        }

        BidRankingMessage.BidRankingMessageBuilder builder = BidRankingMessage.builder()
                .singleRanking(singleRanking).totalRanking(totalRanking).requestId(requestId);

        // 内部推送总排名
        sendMessage(FROM_USER_ID, Convert.toStr(biddingSummaryInfoCache.getTendererUserId()), NumberConstant.FIVE, builder.build());

        // 推送单排名
        if (CollectionUtil.isNotEmpty(singleRanking)) {

            // key: 出价人，value: 排名
            Map<String, List<BidRankingMessage.BidRanking>> bidRankingMap = null;
            if (CollectionUtil.isNotEmpty(totalRanking)) {
                bidRankingMap = totalRanking.stream().collect(Collectors.groupingBy(BidRankingMessage.BidRanking::getOfferUser));
            }

            // 筛选对应外部端的单排名
            Set<BiddingSummaryInfoCache.VendorSummary> vendorSummaries = biddingSummaryInfoCache.getVendorSummaries();
            for (BiddingSummaryInfoCache.VendorSummary vendorSummary : vendorSummaries) {

                if (CollectionUtil.isNotEmpty(bidRankingMap) && bidRankingMap.containsKey(vendorSummary.getVendorUserName())
                        && CollectionUtil.isNotEmpty(categoryBiddingItems.get(vendorSummary.getVendorUserName()))
                        && categoryBiddingItems.get(vendorSummary.getVendorUserName()).size() == biddingSummaryInfoCache.getTotalQuantityArticles()) {
                    builder.totalRanking(bidRankingMap.get(vendorSummary.getVendorUserName()));
                }

                Map<String, List<BidRankingMessage.BidRanking>> singleRankingMap = MapUtil.newHashMap();
                for (Map.Entry<String, List<BidRankingMessage.BidRanking>> entry : singleRanking.entrySet()) {
                    Map<String, List<BidRankingMessage.BidRanking>> tempMap = entry.getValue().stream().collect(Collectors.groupingBy(BidRankingMessage.BidRanking::getOfferUser));
                    if (tempMap.containsKey(vendorSummary.getVendorUserName())) {
                        singleRankingMap.put(entry.getKey(), tempMap.get(vendorSummary.getVendorUserName()));
                    }

                    builder.singleRanking(singleRankingMap);
                    sendMessage(Convert.toStr(biddingSummaryInfoCache.getTendererUserId()), Convert.toStr(vendorSummary.getVendorUserId()), NumberConstant.FIVE, builder.build());
                    singleRankingMap.clear();
                }
            }
        }
    }

    /**
     * 获取单排名
     *
     * @param offers    提供了
     * @param direction 方向
     * @return {@link Map}<{@link String}, {@link List}<{@link BidRankingMessage.BidRanking}>> key: 物品标识，value: 排名
     */
    private Map<String, List<BidRankingMessage.BidRanking>> getSingleRanking(List<OfferVO> offers, Set<BiddingSummaryInfoCache.VendorSummary> vendorSummaries, Boolean direction) {

        Map<String, List<OfferVO>> offerMap = offers.stream().collect(Collectors.groupingBy(OfferVO::getOfferGoods));
        return offerMap.entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey,
                        a -> offerService.computingRankings(a.getValue(), direction).stream()
                                .map(c -> setBidRanking(c, vendorSummaries)).collect(Collectors.toList())));
    }

    /**
     * 获取总计排名
     *
     * @param offers    提供了
     * @param direction 方向
     * @return {@link List}<{@link BidRankingMessage.BidRanking}>
     */
    private List<BidRankingMessage.BidRanking> getTotalRanking(List<OfferVO> offers, Set<BiddingSummaryInfoCache.VendorSummary> vendorSummaries, Boolean direction) {
        return offerService.computingRankings(offers, direction).stream()
                .map(c -> setBidRanking(c, vendorSummaries)).collect(Collectors.toList());
    }

    /**
     * 设置报价排名
     *
     * @param offerVO         提供VO
     * @param vendorSummaries 供应商总结
     * @return {@link BidRankingMessage.BidRanking}
     */
    private BidRankingMessage.BidRanking setBidRanking(OfferVO offerVO, Set<BiddingSummaryInfoCache.VendorSummary> vendorSummaries) {

        BidRankingMessage.BidRanking bidRanking = BeanUtil.copyProperties(offerVO, BidRankingMessage.BidRanking.class);
        if (CollectionUtil.isNotEmpty(vendorSummaries)) {
            vendorSummaries.stream()
                    .filter(a -> Validator.equal(a.getVendorId(), bidRanking.getVendorId())).findAny()
                    .ifPresent(a -> {
                        bidRanking.setVendorMark(a.getVendorMark());
                        bidRanking.setVendorName(a.getVendorName());
                    });
        }

        return bidRanking;
    }

    /**
     * 获取竞价方向
     *
     * @param paidDirection 竞价方向
     * @return {@link Boolean} 竞价方向：true: 正向，false: 反向
     */
    private Boolean getPaidDirection(String paidDirection) {
        return DirectionOfEnum.POSITIVE.getValue().equals(paidDirection) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 获取报价结构
     *
     * @param bidStructure 报价结构
     * @return {@link Boolean} 报价结构 true: 多物品竞价, false: 单物品
     */
    private Boolean getBidStructure(String bidStructure){
        return BidStructure.MORE_ITEMS.getValue().equals(bidStructure) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 计算出价
     *
     * @param biddingMessage 竞价信息
     */
    private void calculationOffer(BiddingMessage biddingMessage) {

        PaidSheetVO paidSheetVO = paidSheetService.findPaidSheetByRequestId(biddingMessage.getRequestId());
        if (ObjectUtil.isNull(paidSheetVO)) {
            setDefaultValue(biddingMessage);
            return;
        }

        // 竞价方向：true: 正向，false: 反向
        Boolean direction = getPaidDirection(paidSheetVO.getPaidDirection());

        List<PaidMaterialVO> paidMaterialVOList = paidSheetVO.getPaidMaterialVOS();
        if (CollectionUtil.isEmpty(paidMaterialVOList)) {
            setDefaultValue(biddingMessage);
            return;
        }

        PaidMaterialVO paidMaterialVO = paidMaterialVOList.stream()
                .filter(a -> StrUtil.equals(biddingMessage.getOfferGoods(), Convert.toStr(a.getId())))
                .findAny().orElse(null);

        if (ObjectUtil.isNull(paidMaterialVO)) {
            setDefaultValue(biddingMessage);
            return;
        }

        Double money = getNumber(Convert.toDouble(biddingMessage.getMoney()));
        Double lastMoney;

        // 是否触发阈值，0:否，1：有
        Byte triggerThreshold  = NumberConstant.ZERO.byteValue();
        List<OfferVO> offerVOList = offerService.findOffer(biddingMessage.getRequestId(), biddingMessage.getOfferGoods(), NumberConstant.ONE.byteValue(), biddingMessage.getOfferUser());
        if (CollectionUtil.isNotEmpty(offerVOList)) {
            OfferVO offerVO = offerVOList.get(offerVOList.size() - NumberConstant.ONE);
            lastMoney = getNumber(Double.valueOf(offerVO.getMoney()));
            triggerThreshold = offerVO.getTriggerThreshold();
        }else {
            lastMoney = StrUtil.equals(ZtbConstant.DISTINGUISH, paidSheetVO.getStartPriceRule()) ?
                    paidVendorService.findPaidVendorVOByUserNameAndRequestId(biddingMessage.getOfferUser(),
                            biddingMessage.getRequestId()).getStartPrice() : paidMaterialVO.getStartPrice();
        }

        Double minimumBidRange = getNumber(paidMaterialVO.getMinimumBidRange());
        String minimumBidRule = paidMaterialVO.getMinimumBidRule();
        Double triggerMinimumBidRange = getNumber(paidMaterialVO.getTriggerMinimumBidRange());

        //达到触发价格
        if (Validator.equal(NumberConstant.ONE.byteValue(), triggerThreshold)) {
            if (getPaidRule(minimumBidRule)) {
                biddingMessage.setAvailable(calculationEffective(money, direction ? (lastMoney + triggerMinimumBidRange) : (lastMoney - triggerMinimumBidRange), direction));
            } else {
                biddingMessage.setAvailable(calculationEffective(money, lastMoney, triggerMinimumBidRange, direction));
            }
            biddingMessage.setTriggerThreshold(triggerThreshold);
        } else {
            if (getPaidRule(minimumBidRule)) {
                biddingMessage.setAvailable(calculationEffective(money, direction ? (lastMoney + minimumBidRange) : (lastMoney - minimumBidRange), direction));
            } else {
                biddingMessage.setAvailable(calculationEffective(money, lastMoney, minimumBidRange, direction));
            }
            //判断是否达到触发价格
            biddingMessage.setTriggerThreshold(calculationTriggerThreshold(money, paidMaterialVO.getTriggerPrice(), direction));
        }

        biddingMessage.setDescription(getInvalidReason(typeCast(biddingMessage.getAvailable()), Boolean.FALSE));
    }

    /**
     * 获取无效的原因
     *
     * @param isAvailable 是可用的
     * @param isFirst     是否第一次
     * @return {@link String}
     */
    private String getInvalidReason(Boolean isAvailable, Boolean isFirst) {
        if (!isAvailable) {
            return isFirst ? ZtbConstant.OFFER_DESCRIPTION_START_PRICE : ZtbConstant.OFFER_DESCRIPTION_MIN;
        }
        return null;
    }

    /**
     * 获取 number
     *
     * @param number 数量
     * @return {@link Double}
     */
    private Double getNumber(Double number) {
        if (Validator.isNull(number) || number < NumberConstant.ZERO.doubleValue()) {
            number = NumberConstant.ZERO.doubleValue();
        }

        return number;
    }

    /**
     * 设置默认的值
     *
     * @param biddingMessage 竞价信息
     */
    private void setDefaultValue(BiddingMessage biddingMessage) {
        biddingMessage.setTriggerThreshold(NumberConstant.ZERO.byteValue());
        biddingMessage.setAvailable(NumberConstant.ZERO.byteValue());
    }

    /**
     * 获取支付规则
     *
     * @param bidRule 报价规则
     * @return {@link Boolean} true: 绝对阈值, false: 最小出价倍数
     */
    private Boolean getPaidRule(String bidRule) {
        return StrUtil.equals(BidRuleEnum.ABSOLUTE_THRESHOLD.getValue(), bidRule);
    }

    /**
     * 计算有效
     *
     * @param money           钱
     * @param lastMoney       最后的出价
     * @param minimumBidRange 最低报价范围
     * @param direction       竞价方向：true: 正向，false: 反向
     * @return {@link Byte}  0:否，1：是
     */
    private Byte calculationEffective(Double money, Double lastMoney, Double minimumBidRange, Boolean direction) {
        money = getNumber(money);
        lastMoney = getNumber(lastMoney);
        minimumBidRange = getNumber(minimumBidRange);
        if (direction) {
            return typeCast(money >= (lastMoney + minimumBidRange) && (money - lastMoney) % minimumBidRange == 0 ? Boolean.TRUE : Boolean.FALSE);
        } else {
            return typeCast(money <= (lastMoney - minimumBidRange) && (lastMoney - money) % minimumBidRange == 0 ? Boolean.TRUE : Boolean.FALSE);
        }
    }

    /**
     * 计算有效
     *
     * @param money        钱
     * @param direction    竞价方向：true: 正向，false: 反向
     * @param compareMoney 出价
     * @return {@link Byte} 0:否，1：是
     */
    private Byte calculationEffective(Double money, Double compareMoney, Boolean direction) {
        money = getNumber(money);
        compareMoney = getNumber(compareMoney);
        return direction ? typeCast(money >= compareMoney ? Boolean.TRUE : Boolean.FALSE)
                : typeCast(money <= compareMoney ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * 计算触发阈值
     *
     * @param money          钱
     * @param direction      方向
     * @param thresholdValue 阈值
     * @return {@link Byte}
     */
    private Byte calculationTriggerThreshold(Double money, Double thresholdValue, Boolean direction) {
        money = getNumber(money);
        thresholdValue = getNumber(thresholdValue);
        return direction ? typeCast(money >= thresholdValue ? Boolean.TRUE : Boolean.FALSE)
                : typeCast(money <= thresholdValue ? Boolean.TRUE : Boolean.FALSE);

    }

    /**
     * 类型转换
     *
     * @param flag 标志
     * @return {@link Byte} 0:否，1：是
     */
    private Byte typeCast(Boolean flag) {
        return flag ? NumberConstant.ONE.byteValue() : NumberConstant.ZERO.byteValue();
    }

    /**
     * 类型转换
     *
     * @param flag 标志
     * @return {@link Boolean}
     */
    private Boolean typeCast(int flag) {
        return Validator.equal(NumberConstant.ONE, flag) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 发送消息
     *
     * @param message    消息
     * @param fromUserId 发送人
     * @param type       类型
     * @param toUserId   接收人
     */
    private void sendMessage(String fromUserId, String toUserId, Integer type, Object message) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setMessage(message);
        sendMessage.setFromUserId(fromUserId);
        sendMessage.setType(type);

        String jsonString = JSON.toJSONString(sendMessage, SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat);
        WebSocketServer.sendMessage(jsonString, toUserId);
    }

    /**
     * 发送消息
     *
     * @param fromUserId 发送人
     * @param toUserIds  接收人
     * @param type       类型
     * @param message    消息
     */
    private void sendMessage(String fromUserId, Set<String> toUserIds, Integer type, Object message) {

        if (CollectionUtil.isNotEmpty(toUserIds)) {
            for (String toUserId : toUserIds) {
                sendMessage(fromUserId, toUserId, type, message);
            }
        }
    }

    /**
     * 获取竞价单信息缓存
     *
     * @return {@link List}<{@link PaidSheetInfoVO}>
     */
    private List<PaidSheetInfoVO> getPaidSheetInfosCache() {
        return redisUtils.get(CacheConstant.PAIDS, new TypeReference<List<PaidSheetInfoVO>>() {
        });
    }

    /**
     * 获取投标总结信息缓存
     *
     * @return {@link List}<{@link BiddingSummaryInfoCache}>
     */
    private List<BiddingSummaryInfoCache> getBiddingSummaryInfoCache() {
        return redisUtils.get(CacheConstant.BIDDING_SUMMARY_CACHE, new TypeReference<List<BiddingSummaryInfoCache>>(){});
    }

















}
