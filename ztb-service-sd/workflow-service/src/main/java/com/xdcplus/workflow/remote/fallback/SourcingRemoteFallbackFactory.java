package com.xdcplus.workflow.remote.fallback;

import com.xdcplus.workflow.remote.SourcingRemote;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 寻源服务远程调用降级
 *
 * @author Rong.Jia
 * @date 2021/09/28
 */
@Slf4j
@Component
public class SourcingRemoteFallbackFactory implements FallbackFactory<SourcingRemote> {

    @Override
    public SourcingRemote create(Throwable throwable) {

        return new SourcingRemote() {
            @Override
            public ResponseVO<Long> getCustomToUserId(String mark, Long requestId) {
                log.error("getCustomToUserId {}", throwable.getMessage());
                return ResponseVO.error();
            }
        };
    }
}
