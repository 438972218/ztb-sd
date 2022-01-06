package com.xdcplus.vendorperm.remote.service;

import com.xdcplus.ztb.common.remote.domain.workflow.dto.ExtraMailNotificationInfoDTO;

/**
 * 工作流程服务
 *
 * @author Fish.Fei
 * @date 2021/09/16
 */
public interface WorkFlowService {

    void emailNotification(ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO);
}
