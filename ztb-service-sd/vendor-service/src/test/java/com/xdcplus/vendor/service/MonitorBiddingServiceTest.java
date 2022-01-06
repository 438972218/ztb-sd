package com.xdcplus.vendor.service;

import com.xdcplus.vendor.VendorApplicationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class MonitorBiddingServiceTest extends VendorApplicationTest {

    @Autowired
    private MonitorBiddingService monitorBiddingService;

    @Test
    void messageHandler() {

        String s = "{\"type\":1,\"message\":{\"money\":10000,\"requestId\":\"1439146952953503746\",\"offerGoods\":\"1439147206596710401\",\"offerUser\":\"gongying2\",\"children\":[]}}";


        monitorBiddingService.messageHandler(s, "1439131443641053186");
    }

    @Test
    void sendClientState() {
    }

    @Test
    void sendBiddingTime() {
    }

    @Test
    void refreshBiddingSummaryInfoCache() {
    }
}