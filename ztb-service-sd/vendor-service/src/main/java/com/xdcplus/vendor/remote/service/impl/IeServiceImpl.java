package com.xdcplus.vendor.remote.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xdcplus.vendor.remote.IeRequestRemote;
import com.xdcplus.vendor.remote.service.IeService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.*;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.ProcessConfigVO;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
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
@Service("ieRequestService")
public class IeServiceImpl implements IeService {

    @Autowired
    IeRequestRemote ieRequestRemote;

    @Override
    public PageVO<RequestVO> findRequest(RequestFilterDTO requestFilterDTO) {
        ResponseVO<PageVO<RequestVO>> responseVO = ieRequestRemote.findRequest(requestFilterDTO);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("getRequestVO() get api findRequest failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_REQUEST_FLOWVO_FAIL, responseVO.getMessage());
        }

        return ResponseUtils.getResponse(responseVO, new TypeReference<PageVO<RequestVO>>(){});
    }

    @Override
    public List<RequestVO> findRequestByParentId(Long parentId) {
        RequestFilterDTO requestFilterDTO =new RequestFilterDTO();
        requestFilterDTO.setParentId(parentId);
        requestFilterDTO.setCurrentPage(-1);
        ResponseVO<PageVO<RequestVO>> responseVO = ieRequestRemote.findRequest(requestFilterDTO);

        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("getRequestFlowVO() get api findRequestByParentId failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_REQUEST_FLOWVO_FAIL, responseVO.getMessage());
        }

        return ResponseUtils.getResponse(responseVO, new TypeReference<PageVO<RequestVO>>(){}).getRecords();
    }

    @Override
    public RequestVO findRequestById(Long requestId) {
        ResponseVO<RequestVO> responseVO = ieRequestRemote.findRequestById(requestId);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("getRequestFlowVO() get api findRequestById failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_REQUEST_FLOWVO_FAIL, requestId+":"+responseVO.getMessage());
        }

        return ResponseUtils.getResponse(responseVO, RequestVO.class);
    }

    @Override
    public PageVO<ProcessConfigVO> findProcessConfigFilter(ProcessConfigFilterDTO processConfigFilterDTO) {
        ResponseVO<PageVO<ProcessConfigVO>> responseVO = ieRequestRemote.findProcessConfigFilter(processConfigFilterDTO);
        if (!NumberConstant.ZERO.equals(responseVO.getCode()) || ObjectUtil.isNull(responseVO.getData())) {

            log.error("findProcessConfigFilter failed");
            return null;
//            throw new ZtbWebException(ResponseEnum.API_REQUEST_FLOWVO_FAIL, responseVO.getMessage());
        }

        return ResponseUtils.getResponse(responseVO, new TypeReference<PageVO<ProcessConfigVO>>(){});
    }

    @Override
    public void emailNotification(ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO) {
        ResponseVO<String> result = ieRequestRemote.emailNotification(extraMailNotificationInfoDTO);
        if (result != null) {
            log.info("发送邮件返回报文：{}", JSON.toJSONString(result));
        }
        if (result == null
            //特殊情况：发送邮件有延时
//                || StringUtils.isBlank(result.getData())
        ) {
            log.info("邮件发送失败，请求报文：{}，返回报文：{}：", JSON.toJSONString(extraMailNotificationInfoDTO), JSON.toJSONString(result));
            throw new ZtbWebException(-1, "邮件发送失败");
        }
    }

}
