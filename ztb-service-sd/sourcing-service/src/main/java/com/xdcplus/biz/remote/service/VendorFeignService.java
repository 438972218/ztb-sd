package com.xdcplus.biz.remote.service;

import com.xdcplus.ztb.common.remote.domain.sourcing.BidRankingMessageInfo;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.ProcessConfigVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestFlowVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;

import java.util.List;

/**
 * 供应商服务
 *
 * @author Fish.Fei
 * @date 2021/08/16
 */
public interface VendorFeignService {

    Boolean deleteByRequestId(Long requestId);

    BidRankingMessageInfo getRankingByVendorUserIdAndRequestId(Long vendorUserId, Long requestId);

}
