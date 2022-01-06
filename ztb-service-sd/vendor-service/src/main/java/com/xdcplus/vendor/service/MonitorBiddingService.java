package com.xdcplus.vendor.service;

import javax.annotation.Nullable;

/**
 * 监测招标服务
 *
 * @author Rong.Jia
 * @date 2021/08/23
 */
public interface MonitorBiddingService {

    /**
     * 消息处理程序
     *
     * @param message         消息
     * @param userId 用户ID
     */
    void messageHandler(String message, String userId);

    /**
     * 推送客户端状态
     */
    void sendClientState();

    /**
     * 推送竞价时间
     * @param userId 用户ID
     * @param requestId 表单ID
     * @param finalBiddingTime 最后竞价时间
     */
    void sendBiddingTime(@Nullable Long requestId, @Nullable String userId, Long finalBiddingTime);

    /**
     * 刷新竞价简要信息
     */
    void refreshBiddingSummaryInfoCache();

    /**
     * 添加招标时间定时任务
     */
    void addBiddingTimeTimedTask();

    /**
     * 更新供应商标识
     */
    void refreshVendorMark();










}
