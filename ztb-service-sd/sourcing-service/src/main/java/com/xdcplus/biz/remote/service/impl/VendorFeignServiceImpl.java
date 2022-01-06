package com.xdcplus.biz.remote.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.xdcplus.biz.remote.VendorRemote;
import com.xdcplus.biz.remote.service.VendorFeignService;
import com.xdcplus.ztb.common.remote.domain.sourcing.BidRankingMessageInfo;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.ProcessConfigVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestFlowVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import com.xdcplus.ztb.common.tool.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 即服务impl
 * 即请求服务impl
 *
 * @author Fish.Fei
 * @date 2021/08/16
 */
@Slf4j
@Service("vendorFeignService")
public class VendorFeignServiceImpl implements VendorFeignService {

    @Autowired
    VendorRemote vendorRemote;

    @Override
    public Boolean deleteByRequestId(Long requestId) {
        ResponseVO<Boolean> responseVO = vendorRemote.deleteByRequestId(requestId);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("deleteByRequestId failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_REQUEST_FLOWVO_FAIL, responseVO.getMessage());
        }

        return ResponseUtils.getResponse(responseVO);
    }

    @Override
    public BidRankingMessageInfo getRankingByVendorUserIdAndRequestId(Long vendorUserId, Long requestId) {
        ResponseVO<BidRankingMessageInfo> responseVO = vendorRemote.getRankingByVendorUserIdAndRequestId(vendorUserId,requestId);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

//            log.error("getRankingByVendorUserNameAndRequestId failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_REQUEST_FLOWVO_FAIL, responseVO.getMessage());
        }

        return ResponseUtils.getResponse(responseVO, BidRankingMessageInfo.class);
    }
}
