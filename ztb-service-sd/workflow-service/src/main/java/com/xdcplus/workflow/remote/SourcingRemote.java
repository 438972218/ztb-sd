package com.xdcplus.workflow.remote;

import com.xdcplus.workflow.remote.fallback.SourcingRemoteFallbackFactory;
import com.xdcplus.ztb.common.tool.constants.ServiceConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 寻源服务远程调用
 *
 * @author Rong.Jia
 * @date 2021/08/03 16:21:01
 */
@Component
@RequestMapping("/")
@FeignClient(value = ServiceConstant.SOURCING_SERVICE, fallbackFactory = SourcingRemoteFallbackFactory.class)
public interface SourcingRemote {

    /**
     * 根据标识和requestId查询出toUserId
     * @param mark
     * @param requestId
     * @return
     */
    @GetMapping(value = "custom/getCustomToUserId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<Long> getCustomToUserId(@RequestParam("mark") String mark, @RequestParam("requestId") Long requestId);
}
