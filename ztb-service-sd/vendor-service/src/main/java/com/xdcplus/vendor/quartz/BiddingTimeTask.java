package com.xdcplus.vendor.quartz;

import cn.hutool.core.date.DateUtil;
import com.xdcplus.vendor.service.MonitorBiddingService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 投标时间的任务
 *
 * @author Rong.Jia
 * @date 2021/10/14
 */
@Slf4j
public class BiddingTimeTask extends QuartzJobBean {

    @Autowired
    private MonitorBiddingService monitorBiddingService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        monitorBiddingService.sendBiddingTime(null, null, DateUtil.current());
    }
}
