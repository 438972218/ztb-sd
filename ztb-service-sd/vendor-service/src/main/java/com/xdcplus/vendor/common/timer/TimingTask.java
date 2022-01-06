package com.xdcplus.vendor.common.timer;

import cn.hutool.core.date.DateUtil;
import com.xdcplus.vendor.service.MonitorBiddingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author Rong.Jia
 * @date 2021/08/23
 */
@Slf4j
@Component
public class TimingTask {

    @Autowired
    private MonitorBiddingService monitorBiddingService;

    /**
     * 广播websocket 客户端状态
     *  每1秒执行一次
     */
    @Scheduled(fixedDelay = 5 * 1000)
    public void broadcastClientState() {
        log.debug("broadcastClientState : {}", System.currentTimeMillis());
        monitorBiddingService.sendClientState();
    }

    /**
     * 刷新竞价简要信息
     *  每1秒执行一次
     */
    @Scheduled(fixedDelay = 1000)
    public void refreshBiddingSummaryInfoCache() {
        log.debug("refreshBiddingSummaryInfoCache : {}", System.currentTimeMillis());
        monitorBiddingService.refreshBiddingSummaryInfoCache();
    }

    /**
     * 刷新刷新竞价时间信息
     *  每1秒执行一次
     */
    @Scheduled(fixedDelay = 1000)
    public void refreshBiddingTime() {
        log.debug("refreshBiddingTime : {}", System.currentTimeMillis());
        monitorBiddingService.sendBiddingTime(null, null, DateUtil.current());
    }

    /**
     * 刷新供应商标识
     * 每20分钟一次
     */
    @Scheduled(fixedDelay = 1000 * 60 * 20)
    public void refreshVendorMark() {
        log.debug("refreshVendorMark : {}", System.currentTimeMillis());
        monitorBiddingService.refreshVendorMark();
    }








}
