package com.xdcplus.workflow.remote.fallback;

import com.xdcplus.workflow.remote.VendorPermRemote;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 外部权限远程调用降级
 *
 * @author Rong.Jia
 * @date 2021/10/13
 */
@Slf4j
@Component
public class VendorPermRemoteFallbackFactory implements FallbackFactory<VendorPermRemote> {
    @Override
    public VendorPermRemote create(Throwable throwable) {
        return new VendorPermRemote() {
            @Override
            public ResponseVO<Map<String, String>> queryVendorUserMap() {
                log.error("queryVendorUserMap {}", throwable.getMessage());
                return ResponseVO.error();
            }
        };
    }
}
