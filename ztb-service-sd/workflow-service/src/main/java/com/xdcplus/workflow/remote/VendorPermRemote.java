package com.xdcplus.workflow.remote;

import com.xdcplus.workflow.remote.fallback.VendorPermRemoteFallbackFactory;
import com.xdcplus.ztb.common.tool.constants.ServiceConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 外部权限远程调用
 *
 * @author Rong.Jia
 * @date 2021/08/03 16:21:01
 */
@Component
@RequestMapping("/apivendor")
@FeignClient(value = ServiceConstant.VENDOR_PERM_SERVICE, fallbackFactory = VendorPermRemoteFallbackFactory.class)
public interface VendorPermRemote {

    /**
     * 查询出所有的用户, key: 账号，value: 名称
     *
     * @return {@link ResponseVO}<{@link Map}<{@link String}, {@link String}>>
     */
    @GetMapping(value = "queryVendorUserMap", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<Map<String,String>> queryVendorUserMap();


}
