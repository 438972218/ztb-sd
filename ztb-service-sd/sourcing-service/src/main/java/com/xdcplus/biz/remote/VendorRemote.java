package com.xdcplus.biz.remote;

import cn.hutool.extra.validation.ValidationUtil;
import com.xdcplus.biz.remote.fallback.IeRequestRemoteFallbackFactory;
import com.xdcplus.ztb.common.remote.domain.sourcing.BidRankingMessageInfo;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.ProcessConfigVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestFlowVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.ServiceConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 供应商远程
 *
 * @author Fish.Fei
 * @date 2021/08/12
 */
@Component
@RequestMapping("/")
@FeignClient(value = ServiceConstant.VENDOR_SERVICE, fallbackFactory = IeRequestRemoteFallbackFactory.class)
public interface VendorRemote {

    /**
     * 根据requestId删除出价记录
     *
     * @param requestId 请求id
     * @return {@link ResponseVO}
     */
    @DeleteMapping(value = "offer/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO deleteByRequestId(@PathVariable("requestId")
                                        @NotNull(message = "表单ID不能为空") Long requestId);

    /**
     * 根据供应商用户ID查询出价排名
     * @param vendorUserId
     * @param requestId
     * @return
     */
    @GetMapping(value = "paidMaterial/lastRankingByVendor", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseVO<BidRankingMessageInfo> getRankingByVendorUserIdAndRequestId(@RequestParam("vendorUserId") Long vendorUserId,
                                                                           @RequestParam("requestId") Long requestId);

}
