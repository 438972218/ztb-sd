package com.xdcplus.biz.common.scheduling;

import com.xdcplus.biz.service.BidSheetService;
import com.xdcplus.biz.service.PaidSheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
//@Profile("pro")
public class TimingTask {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private PaidSheetService paidSheetService;

    @Autowired
    private BidSheetService bidSheetService;

    /**
     * 刷新（进行中/暂停）竞价单信息
     * 定时：每3秒刷新一次
     */
    @Scheduled(fixedDelay = 3 * 1000)
    public void refreshPaidSheetCache() {

        log.debug("refreshPaidSheetCache {}", System.currentTimeMillis());

        taskExecutor.execute(() -> paidSheetService.queryPaidSheetByMonitor());

    }

    /**
     * 监听竞价单开始时间
     * 定时：每1秒刷新一次
     */
    @Scheduled(fixedDelay = 1 * 1000)
    public void refreshStartPaidSheet() {

        log.debug("refreshStartPaidSheet {}", System.currentTimeMillis());

        taskExecutor.execute(() -> paidSheetService.startPaidSheet());

    }

    /**
     * 监听招标单结束时间
     * 发送邮件
     * 定时：每5秒刷新一次
     */
    @Scheduled(fixedDelay = 5 * 1000)
    public void refreshFinishBidSheet() {

        log.debug("refreshFinishBidSheet {}", System.currentTimeMillis());
        taskExecutor.execute(() -> bidSheetService.finishBidSheet());

    }

//    /**
//     * 刷新运行中的竞价单中供应商标识
//     * 定时：每20分钟刷新一次
//     */
////    @Scheduled(fixedDelay = 20 * 60 * 1000)
//    @Scheduled(cron = "0 0,20,40 * * * ?")
//    public void refreshPaidVendorMark() {
//
//        log.debug("refreshPaidVendorMark {}", System.currentTimeMillis());
//
//        taskExecutor.execute(() -> paidSheetService.refreshPaidVendorMark());
//
//    }

}
