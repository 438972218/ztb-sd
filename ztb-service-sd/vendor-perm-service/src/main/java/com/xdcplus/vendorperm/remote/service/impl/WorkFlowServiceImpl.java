package com.xdcplus.vendorperm.remote.service.impl;

import com.alibaba.fastjson.JSON;
import com.xdcplus.vendorperm.remote.EmailRemote;
import com.xdcplus.vendorperm.remote.service.WorkFlowService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.ExtraMailNotificationInfoDTO;
import com.xdcplus.ztb.common.tool.pojo.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("workFlowService")
public class WorkFlowServiceImpl implements WorkFlowService {

    @Autowired
    EmailRemote emailRemote;

    @Override
    public void emailNotification(ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO) {
        ResponseVO<String> result= emailRemote.emailNotification(extraMailNotificationInfoDTO);
        if(result!=null){
            log.info("发送邮件返回报文：{}", JSON.toJSONString(result));
        }
        if(result==null
                //特殊情况：发送邮件有延时
//                || StringUtils.isBlank(result.getData())
        ){
            log.info("邮件发送失败，请求报文：{}，返回报文：{}：",JSON.toJSONString(extraMailNotificationInfoDTO),JSON.toJSONString(result));
            throw new ZtbWebException(-1,"邮件发送失败");
        }
    }
}
