package com.xdcplus.biz.remote.fallback;

import com.xdcplus.biz.remote.IeRequestRemote;
import com.xdcplus.biz.remote.VendorRemote;
import com.xdcplus.ztb.common.remote.domain.sourcing.BidRankingMessageInfo;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.ProcessConfigVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestFlowVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@Component
public class VendorRemoteFallbackFactory implements FallbackFactory<VendorRemote> {

    @Override
    public VendorRemote create(Throwable throwable) {
        return new VendorRemote() {
            @Override
            public ResponseVO deleteByRequestId(@NotNull(message = "表单ID不能为空") Long requestId) {
                log.error("deleteByRequestId {}", throwable.getMessage());
                return ResponseVO.error();
            }

            @Override
            public ResponseVO<BidRankingMessageInfo> getRankingByVendorUserIdAndRequestId(Long vendorUserId, Long requestId) {
                log.error("getRankingByVendorUserIdAndRequestId {}", throwable.getMessage());
                return ResponseVO.error();
            }

        };
    }
}
