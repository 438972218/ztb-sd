package com.xdcplus.vendor;

import com.xdcplus.vendor.service.MonitorBiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

/**
 * Spring 启动后的操作
 *
 * @author Fish.Fei
 * @date 2021/05/24 09:24
 */
@Component
public class VendorApplicationRunner implements ApplicationRunner {

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private MonitorBiddingService monitorBiddingService;

    @Override
    public void run(ApplicationArguments args) {
    }
}
