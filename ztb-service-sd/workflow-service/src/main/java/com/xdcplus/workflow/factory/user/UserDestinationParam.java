package com.xdcplus.workflow.factory.user;

import lombok.Builder;
import lombok.Data;

/**
 * 用户去向参数
 *
 * @author Rong.Jia
 * @date 2021/07/19
 */
@Data
@Builder
public class UserDestinationParam {

    /**
     *  表单ID
     */
    private Long requestId;

    /**
     * 流程配置ID
     */
    private Long configId;

    /**
     * 用户去向类型
     */
    private Integer userToType;

    /**
     *  用户标识
     */
    private Long userId;

    /**
     * 添加人
     */
    private String createUserName;
















}
