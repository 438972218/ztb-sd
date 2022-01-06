package com.xdcplus.workflow.service;


import com.xdcplus.workflow.common.pojo.bo.DiscussGroupBO;
import com.xdcplus.workflow.common.pojo.entity.DiscussGroup;
import com.xdcplus.workflow.common.pojo.vo.DiscussGroupVO;
import com.xdcplus.ztb.common.mp.service.BaseService;

import java.util.List;

/**
 * 讨论组服务类
 *
 * @author Rong.Jia
 * @since 2021-08-18
 */
public interface DiscussGroupService extends BaseService<DiscussGroup, DiscussGroupVO, DiscussGroup> {

    /**
     * 查询讨论组
     *
     * @param requestId 表单ID
     * @return {@link List<DiscussGroupBO>} 讨论组
     */
    List<DiscussGroupBO> findDiscussionGroup(Long requestId);

    /**
     * 保存讨论组
     *
     * @param requestId 表单id
     * @param subject   主题
     * @param currentUser 当前用户
     * @return {@link Long}
     */
    Long saveDiscussGroup(Long requestId, String subject, String currentUser);

    /**
     * 查询讨论组信息
     *
     * @param id id
     * @return {@link DiscussGroupVO}
     */
    DiscussGroupVO findOne(Long id);










}
